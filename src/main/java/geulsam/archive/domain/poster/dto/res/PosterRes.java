package geulsam.archive.domain.poster.dto.res;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
    @Schema(example = "2024")
    private int year;
    /**poster 가 DB에 insert 된 날짜*/
    @Schema(example = "2024-05-06T09:00:00")
    private LocalDateTime createdAt;
}
