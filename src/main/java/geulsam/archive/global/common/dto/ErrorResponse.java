package geulsam.archive.global.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
}
