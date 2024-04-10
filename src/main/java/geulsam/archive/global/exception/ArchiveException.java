package geulsam.archive.global.exception;

import lombok.Getter;

@Getter
public class ArchiveException extends RuntimeException{
    private final ErrorCode errorCode;

    /**
     * ErrorCode 를 받아서 에외 생성
     * @param errorCode:  ArchiveException 이 생성된 로직에서 발생한 ErrorCode
     */
    public ArchiveException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 Message를 받아서 예외 생성
     * @param errorCode: ArcheiveException 이 생성된 로직에서 발생한 ErrorCode
     * @param message: 커스텀한 Error 메시지
     */
    public ArchiveException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
