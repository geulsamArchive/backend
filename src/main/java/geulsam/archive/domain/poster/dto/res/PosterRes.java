package geulsam.archive.domain.poster.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Year;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
public class PosterRes {
    /**posterRes 배열의 index*/
    @Schema(example = "0")
    private int id;
    /**poster image 저장 주소 url*/
    @Schema(example = "https://imageURL")
    private String image;
    /**thumbnail image 저장 주소 url*/
    @Schema(example = "https://thumbnailImageURL")
    private String thumbnailImage;
    /**poster 제작자*/
    @Schema(example = "김철수")
    private String designer;
    /**poster 제작 연도*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년")
    @Schema(example = "2024")
    private Year year;
    /**poster 가 DB에 insert 된 날짜*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024-05-06T09:00:00")
    private LocalDateTime createdAt;
}
