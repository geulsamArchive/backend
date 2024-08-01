package geulsam.archive.domain.award.dto.res;

import geulsam.archive.domain.award.entitiy.Award;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AwardRes {
    /**Award 객체의 PK**/
    @Schema(example = "1")
    private Integer awardId;
    /**Award 객체의 이름**/
    @Schema(example = "이번 작품이 처음이라고? 믿을 수 없어 상")
    private String name;
    /**Award 객체의 설명**/
    @Schema(example = "첫 작품을 축하합니다")
    private String explain;
    /**Award 객체의 생성날짜**/
    @Schema(example = "2022-01-01")
    private LocalDate createdAt;

    public AwardRes(Award award) {
        this.awardId = award.getId();
        this.name = award.getName();
        this.explain = award.getExplain();
        this.createdAt = award.getCreatedAt();
    }
}
