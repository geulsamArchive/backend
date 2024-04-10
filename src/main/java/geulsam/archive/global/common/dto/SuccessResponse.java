package geulsam.archive.global.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
public class SuccessResponse<T> {
    @Schema(example = "200")
    /**요청 성공 시 응답 코드*/
    private final int status;
    /**요청 성공 시 메시지*/
    private final String message;
    /**요청 성공 시 데이터가 담길 객체*/
    private final T data;
}
