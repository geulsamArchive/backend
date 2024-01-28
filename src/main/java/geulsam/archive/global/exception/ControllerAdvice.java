package geulsam.archive.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

/** Controller 이후 코드에서 발생하는 에러들을 처리*/
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * RestException 을 받아서 클라이언트에게 http code 400과 에러 미시지로 응답
     * @param e
     * @return
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<RestException> handler(RestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }
}
