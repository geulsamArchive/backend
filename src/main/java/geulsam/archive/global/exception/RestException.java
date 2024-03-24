package geulsam.archive.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** RuntimeException 을 커스텀한 Exctpion*/
@AllArgsConstructor
@Getter
public class RestException extends RuntimeException{

    /** 메시지를 받는 생성자*/
    public RestException(String message) {
        super(message);
    }
}
