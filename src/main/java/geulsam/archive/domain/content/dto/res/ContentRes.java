package geulsam.archive.domain.content.dto.res;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;

@Data
public class ContentRes {
    @Schema(example = "0")
    private int id;
    @Schema(example = "adsa-dsafdsa-vsadf-dsafdsa")
    private String contentId;
    @Schema(example = "소설")
    private String type;
    @Schema(example = "때때로 나는 회색분자라는 소리를 듣는다")
    private String title;
    @Schema(example = "2024")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년")
    private Year year;
    @Schema(example = "하수민")
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년3월10일")
    private LocalDate createdAt;
}
