package geulsam.archive.domain.contentAward.dto.res;

import geulsam.archive.domain.award.entitiy.Award;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentAwardRes {
    @Schema(example = "1")
    private int contentAwardId;
    @Schema(example = "1")
    private int awardId;
    @Schema(example = "이번 작품이 처음이라고? 믿을 수 없어 상")
    private String awardName;
    @Schema(example = "첫 작품을 축하합니다")
    private String awardExplain;
    @Schema(example = "시차와 시대착오")
    private String contentName;
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String contentId;
    @Schema(example = "전하영")
    private String authorName;
    @Schema(example = "POEM")
    private Genre genre;
    @Schema(example = "키워드")
    private String keyword;

    public ContentAwardRes(ContentAward contentAward) {
        Content findContent = contentAward.getContent();
        Award findAward = contentAward.getAward();
        this.contentAwardId = contentAward.getId();
        this.awardId = findAward.getId();
        this.awardName = findAward.getName();
        this.contentName = findContent.getName();
        this.contentId = findContent.getId().toString();
        this.authorName = findContent.getUser().getName();
        this.genre = findContent.getGenre();
        this.keyword = findContent.getUser().getKeyword();
    }
}
