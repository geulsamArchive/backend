package geulsam.archive.domain.content.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.content.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ContentRes {
    @Schema(example = "0")
    private int id;
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String contentId;  //
    @Schema(example = "소설")
    private Genre type;
    @Schema(example = "때때로 나는 회색분자라는 소리를 듣는다")
    private String title;
    @Schema(example = "2024년")
    //JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private int year;  //API 명세서 확인 필요
    @Schema(example = "하수민")
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 3월 10일")
    private LocalDateTime createdAt;
}
