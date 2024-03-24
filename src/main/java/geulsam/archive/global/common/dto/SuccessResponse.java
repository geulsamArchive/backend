package geulsam.archive.global.common.dto;

import lombok.*;

@Data
@Builder
public class SuccessResponse<T> {
    private final int status;
    private final String message;
    private final T data;
}
