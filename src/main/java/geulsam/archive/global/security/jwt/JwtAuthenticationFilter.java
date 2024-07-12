package geulsam.archive.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import geulsam.archive.domain.refreshtoken.repository.RefreshTokenRepository;
import geulsam.archive.global.common.dto.ErrorResponse;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/** jwt 를 사용해서 사용자를 식별하는 필터 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            /* 토큰 추출 */
            String accessToken = extractToken(request, "accessToken");
            String refreshToken = extractToken(request, "refreshToken");

            if (accessToken == null && refreshToken == null) {
                throw new IllegalArgumentException("RefreshToken, AccessToken 없음");
            }

            /* 만약 accessToken 이 존재한다면 */
            if (accessToken != null) {
                /* accessToken 을 사용하여 인증 객체 생성 후 저장 */
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                /* accessToken 의 id 를 사용하여 새로운 accessToken 생성 */
                String id = jwtProvider.getID(accessToken);
                String newAccessToken = jwtProvider.createAccessToken(Integer.valueOf(id));
                response.setHeader("accessToken", "Bearer " + newAccessToken);
                filterChain.doFilter(request, response);
                return;
            }

            if(refreshToken != null ){
                /* refreshToken 을 사용하여 RefreshToken 객체 탐색 */
                RefreshToken refreshTokenInRepo = refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() -> new IllegalArgumentException("재로그인 필요"));

                /* 새로운 RefreshToken 생성 */
                String newRefreshToken = jwtProvider.createRefreshToken();
                String newAccessToken = jwtProvider.createAccessToken(refreshTokenInRepo.getId());

                /* Refresh 리포지토리를 업데이트 */
                refreshTokenInRepo.changeTokenValue(newRefreshToken);
                refreshTokenRepository.save(refreshTokenInRepo);

                /* 인증 객체 생성 후 저장 */
                Authentication authentication = jwtProvider.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                /* 토큰들을 header 에 삽입 */
                response.setHeader("accessToken", "Bearer " + newAccessToken);
                response.setHeader("refreshToken", "Bearer " + newRefreshToken);

                filterChain.doFilter(request, response);
                return;
            }
            // 발생 가능한 에러들 처리
        } catch (UnsupportedJwtException e){
            request.setAttribute("error", new ArchiveException(ErrorCode.AUTH_ERROR, "지원되지 않는 토큰"));
        } catch (MalformedJwtException e) {
            request.setAttribute("error", new ArchiveException(ErrorCode.AUTH_ERROR, "JWT 토큰 손상"));
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", new ArchiveException(ErrorCode.LOGIN_ERROR, e.getMessage()));
        } catch (SignatureException e) {
            request.setAttribute("error", new ArchiveException(ErrorCode.AUTH_ERROR, "토큰 유효성 확인 불가"));
        } catch (ExpiredJwtException e) {
            request.setAttribute("error", new ArchiveException(ErrorCode.LOGIN_ERROR, "로그인 시간 지남"));
        }

        filterChain.doFilter(request, response);
    }

    /**
     * http 서블릿과 추출할 token 이름을 받아서  token 을 추출
     * @param httpServletRequest
     * @param tokenName
     * @return
     */
    public String extractToken(@NotNull HttpServletRequest httpServletRequest, String tokenName){
        return httpServletRequest.getHeader(tokenName);
    }
}
