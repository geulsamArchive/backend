package geulsam.archive.domain.content.dto.res;


import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.content.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ContentInfoRes {
    @Schema(example = "1")
    private String id;
    @Schema(example = "소설")
    private Genre type;
    @Schema(example = "별뜨기에 관하여")
    private String title;
    @Schema(example = "박연준")
    private String author;
    @Schema(example = "fadfadfd-fdfadfa-fadfad")
    private String authorId;
    //sentence?
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 03월 10일")
    private LocalDateTime createdAt;
    @Schema(example = "https://PDF 다운로드 url")
    private String pdf;

}
