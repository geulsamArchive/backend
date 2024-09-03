package geulsam.archive.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import geulsam.archive.global.common.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorityAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ArchiveException archiveException = new ArchiveException(ErrorCode.AUTHORITY_ERROR);

        response.setHeader("Access-Control-Allow-Origin", "*"); // Adjust to specific origins as needed
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "accessToken, refreshToken");

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(archiveException.getErrorCode().getStatus());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(archiveException.getErrorCode().getStatus())
                .error(archiveException.getErrorCode().toString())
                .message(archiveException.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
