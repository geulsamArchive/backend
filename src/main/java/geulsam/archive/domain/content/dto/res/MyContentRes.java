package geulsam.archive.domain.content.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
public class MyContentRes {
    /**Content 객체의 제목*/
    @Schema(example = "때때로 나는 회색분자라는 소리를 듣는다")
    private String title;
    /**Content 객체가 저장된 시각*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 3월 10일", type = "string")
    private LocalDateTime createdAt;
    /**Content 객체의 설명*/
    @Schema(example = "황금시대에나 통하는 수법이지. 비싸고 비이성적일수록 근사하다고 믿는 시대에. 그 아이들이 커서 자기 고향을 어디라고 말할까?")
    private String sentence;
    /**Content 객체의 PK*/
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String contentId;
    /**Content 객체의 장르*/
    @Schema(example = "NOVEL")
    private Genre type;
    /**Content 객체의 */
    @Schema(description = "작품 공개여부", example = "LOGGEDIN")
    private IsVisible isVisible;
    /**Content 객체의 수상이력*/
    @Schema(example = "2023년 글샘문학상, 2024년 글샘문학상")
    private String award;

    public MyContentRes(Content content) {
        this.title = content.getName();
        this.createdAt = content.getCreatedAt();
        this.sentence = content.getSentence();
        this.contentId = content.getId().toString();
        this.type = content.getGenre();
        this.isVisible = content.getIsVisible();
        this.award = content.getAward();
    }
}
