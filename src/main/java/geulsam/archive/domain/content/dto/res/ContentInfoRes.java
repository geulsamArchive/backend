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
public class ContentInfoRes {
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String id;
    @Schema(example = "별뜨기에 관하여")
    private String title;
    @Schema(example = "박연준")
    private String author;
    @Schema(example = "1")
    private int authorId;
    @Schema(example = "황금시대에나 통하는 수법이지. 비싸고 비이성적일수록 근사하다고 믿는 시대에. 그 아이들이 커서 자기 고향을 어디라고 말할까?")
    private String sentence;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 03월 10일", type = "string")
    private LocalDateTime createdAt;
    @Schema(example = "https://example.com")
    private String pdf;
    @Schema(example = "https://example.com")
    private String html;
    @Schema(example = "NOVEL")
    private Genre type;
    @Schema(description = "작품 공개여부", example = "LOGGEDIN")
    private IsVisible isVisible;
    @Schema(example = "2023년 글샘문학상, 2024년 글샘문학상")
    private String award;

    public ContentInfoRes(Content content) {
        this.id = content.getId().toString();
        this.type = content.getGenre();
        this.isVisible = content.getIsVisible();
        this.title = content.getName();
        this.author = content.getUser().getName();
        this.authorId = content.getUser().getId();
        this.sentence = content.getSentence();
        this.createdAt = content.getCreatedAt();
        this.pdf = content.getPdfUrl();
        this.html = content.getHtmlUrl();
        this.award = content.getAward();
    }
}
