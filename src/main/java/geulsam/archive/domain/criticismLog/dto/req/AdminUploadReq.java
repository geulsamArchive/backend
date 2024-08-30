package geulsam.archive.domain.criticismLog.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class AdminUploadReq {
    @Schema(description = "작품 제목", example = "비행운")
    private String contentTitle;
    @Schema(description = "합평이 시행된 시간", example = "2023-08-29T14:30:00")
    private LocalDateTime localDateTime;
    @Schema(description = "클로버노트 URL", example = "https://localhost:8080/aaaa")
    private String cloverNoteUrl;
    @Schema(description = "클로버노트 비밀번호", example = "1q2w3e4r!")
    private String cloverNotePassword;
}
