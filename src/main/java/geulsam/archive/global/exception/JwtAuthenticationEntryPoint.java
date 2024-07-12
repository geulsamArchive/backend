package geulsam.archive.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import geulsam.archive.global.common.dto.ErrorResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * JwtAuthenticationFilter 에서 try-catch 로 잡은 예외를 json 으로 응답.
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // JwtAuthenticationFilter 에서 생성한 예외 추출
        ArchiveException archiveException = (ArchiveException)request.getAttribute("error");

        response.setHeader("Access-Control-Allow-Origin", "*"); // 또는 특정 origin을 설정
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "accessToken, refreshToken");
        
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(archiveException.getErrorCode().getStatus());
        response.setContentType("application/json;charset=UTF-8");

        // 예외 메시지 빌드
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(archiveException.getErrorCode().getStatus())
                .error(archiveException.getErrorCode().toString())
                .message(archiveException.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
