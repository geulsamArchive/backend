package geulsam.archive.global.exception;

import geulsam.archive.global.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 유효성 관련 에러 처리 핸들러
     * @param ex: @VALUED 에서 발생한 에러
     * @param request: 에러가 발생한 http request
     * @return VALUE_ERROR 에러 객체 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("VALUE_ERROR")
                        .message(errors.toString())
                        .build()
                );
    }

    /**
     * ArchiveException 으로 커스텀한 에러 핸들러
     * @param e: 발생한 ArchiveException
     * @return: API_ERROR 에러 객체 응답
     */
    @ExceptionHandler(ArchiveException.class)
    public ResponseEntity<ErrorResponse> handler(ArchiveException e){
        logExceptionDetails(e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("API_ERROR")
                        .message(e.getMessage())
                        .build()
                );
    }

    /**
     * ArchiveException 으로 처리하지 못하는 RuntimeException 를 처리
     * @param e : RuntimeException
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        logExceptionDetails(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse
                                .builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error("INTERNAL_SERVER_ERROR")
                                .message(e.getMessage())
                                .build()
                );
    }

    // 에러 위치 로깅
    private void logExceptionDetails(RuntimeException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement firstElement = stackTrace[0];
            logger.error("Exception occurred in {}.{} at line {}",
                    firstElement.getClassName(),
                    firstElement.getMethodName(),
                    firstElement.getLineNumber()
            );
        }
        logger.error("Full stack trace:", e);
    }
}
