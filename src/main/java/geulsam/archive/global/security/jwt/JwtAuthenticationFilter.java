package geulsam.archive.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import geulsam.archive.domain.refreshtoken.repository.RefreshTokenRepository;
import geulsam.archive.global.common.dto.ErrorResponse;
import geulsam.archive.global.exception.ErrorCode;
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
    protected void doFilterInternal(@org.jetbrains.annotations.NotNull HttpServletRequest request,
                                    @org.jetbrains.annotations.NotNull HttpServletResponse response,
                                    @org.jetbrains.annotations.NotNull FilterChain filterChain) throws ServletException, IOException {
        /* 토큰 추출 */
        String accessToken = extractToken(request, "accessToken");
        String refreshToken = extractToken(request, "refreshToken");


        /* 만약 accessToken 이 존재한다면 */
        if(!(Objects.equals(accessToken, null))){
                /* accessToken 을 사용하여 인증 객체 생성 후 저장*/
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                /* accessToken 의 id 를 사용하여 새로운 accessToken 생성*/
                String id = jwtProvider.getID(accessToken);
                String newAccessToken = jwtProvider.createAccessToken(Integer.valueOf(id));
                response.setHeader("accessToken", "Bearer " + newAccessToken);

            /* filter chain 계속 */
            filterChain.doFilter(request,response);

            /* 만약 refreshToken 이 존재한다면 */
        } else if (!(Objects.equals(refreshToken, null))){
            /* refreshToken 을 사용하여 RefresToken 객체 탐색 */
            RefreshToken refreshTokenInRepo = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("재로그인 필요."));

            /* 새로운 RefreshToken 생성, 탐색한 RefreshToken 객체의 기본키 Id 를 사용하여 새로운 accessToken 생성 */
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

            /* 필터 계속 */
            filterChain.doFilter(request, response);

            return;
        } else {
            filterChain.doFilter(request,response);
        }

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
