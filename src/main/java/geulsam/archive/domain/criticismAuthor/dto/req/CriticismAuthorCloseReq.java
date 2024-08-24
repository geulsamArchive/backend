package geulsam.archive.domain.criticismAuthor.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriticismAuthorCloseReq {
    @Schema(example = "1", description = "합평회 한 작품 ID")
    private String contentId;
    @Schema(example = "https://cloverNote/safsafd", description = "합평회 한 작품 크로버노트")
    private String cloverNoteURL;
    @Schema(example = "1q2w3e4r", description = "합평회 한 작품 크로버노트 비밀번호")
    private String cloverNotePassword;
}
