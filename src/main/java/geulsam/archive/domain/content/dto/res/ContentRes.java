package geulsam.archive.domain.content.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
public class ContentRes {
    /**Content Response 객체 인덱스*/
    @Schema(example = "0")
    private int id;
    /**Content 객체의 PK*/
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String contentId;
    /**Content 객체의 장르*/
    @Schema(example = "소설")
    private Genre type;
    /**Content 객체의 제목*/
    @Schema(example = "때때로 나는 회색분자라는 소리를 듣는다")
    private String title;
    /**Content 객체의 수상 정도*/
    @Schema(example = "2024년 글샘문학상 수상", nullable = true)
    private String award;
    /**Content 객체의 저자*/
    @Schema(example = "하수민")
    private String author;
    /**Content 객체가 저장된 시각*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 3월 10일", type = "string")
    private LocalDateTime createdAt;

    public ContentRes(Content content, int id) {
        this.id = id;
        this.contentId = content.getId().toString();
        this.type = content.getGenre();
        this.title = content.getName();
        this.award = "(예시)2024년 글샘문학상 수상";
        this.author = content.getUser().getName();
        this.createdAt = content.getCreatedAt();
    }
}