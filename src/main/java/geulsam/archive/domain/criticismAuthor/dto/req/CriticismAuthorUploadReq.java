package geulsam.archive.domain.criticismAuthor.dto.req;

import geulsam.archive.domain.content.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CriticismAuthorUploadReq {
    @Schema(example = "1", description = "합평회 아이디")
    private int criticismId;
    @Schema(example = "2", description = "합평회 신청하는 순서")
    @Positive
    private int order;
    @Schema(description = "신청하려는 장르", example = "NOVEL")
    private Genre genre;
}
