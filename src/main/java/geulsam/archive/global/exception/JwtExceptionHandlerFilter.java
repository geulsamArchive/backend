package geulsam.archive.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import geulsam.archive.global.common.dto.ErrorResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (ExpiredJwtException e){ 
            //유효기간 지난 JWT 관련 ERROR 처리
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(ErrorCode.LOGIN_ERROR.getStatus());
            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write(
                    objectMapper
                            .writeValueAsString(
                                    ErrorResponse
                                            .builder()
                                            .status(ErrorCode.LOGIN_ERROR.getStatus())
                                            .message(ErrorCode.LOGIN_ERROR.getMessage())
                                            .error(ErrorCode.LOGIN_ERROR.getError())
                                            .build())
            );
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            // 처음부터 유효하지 않았던 JWT 관련 에러 처리
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(ErrorCode.AUTH_ERROR.getStatus());
            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write(
                    objectMapper
                            .writeValueAsString(
                                    ErrorResponse
                                            .builder()
                                            .status(ErrorCode.AUTH_ERROR.getStatus())
                                            .message(ErrorCode.AUTH_ERROR.getMessage())
                                            .error(ErrorCode.AUTH_ERROR.getError())
                                            .build())
            );
        }
    }
}
