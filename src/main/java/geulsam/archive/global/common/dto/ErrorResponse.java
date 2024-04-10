package geulsam.archive.global.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    /**요청 실패 시 응답 코드*/
    private final int status;
    /**요청 실패 시 에러 상태(ErrorCode 에서 확인 가능)*/
    private final String error;
    /**요청 실패 시 에러 메시지(ErrorCode 에서 확인 가능*/
    private final String message;
}
