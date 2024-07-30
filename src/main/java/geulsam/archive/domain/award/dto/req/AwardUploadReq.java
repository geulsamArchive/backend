package geulsam.archive.domain.award.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class AwardUploadReq {
    @Schema(description = "상 이름", example = "이번 작품이 처음이라고? 믿을 수 없어 상")
    private String name;
    @Schema(description = "상 설명", example = "첫 작품을 축하합니다")
    private String explain;
}
