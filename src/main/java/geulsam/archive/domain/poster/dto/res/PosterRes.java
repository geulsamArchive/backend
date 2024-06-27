package geulsam.archive.domain.poster.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.poster.entity.Poster;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.Year;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class PosterRes {
    /**posterRes 배열의 index*/
    @Schema(description = "페이지 내의 Poster 객체 순서", example = "0")
    private int id;
    /**poster image 저장 주소 url*/
    @Schema(description = "poster 의 저장 주소", example = "https://imageURL")
    private String image;
    /**thumbnail image 저장 주소 url*/
    @Schema(description = "poster thumbnail 의 저장 주소",example = "https://thumbnailImageURL")
    private String thumbnailImage;
    /**poster 제작자*/
    @Schema(description = "poster 제작자", example = "김철수")
    private String designer;
    /**poster 제작 연도*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년")
    @Schema(description = "poster 가 사용된 연도", example = "2024년", type = "string")
    private Year year;
    /**poster 가 DB에 insert 된 날짜*/
    @Schema(description = "poster 가 DB 에 insert 된 날짜", example = "2024년 5월 6일", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    private LocalDateTime createdAt;

    /**
     * Poster 객체를 PosterRes 객체로 변환
     * @param poster
     * @param id
     */
    public PosterRes(Poster poster, int id){
        this.id = id;
        this.image = poster.getUrl();
        this.thumbnailImage = poster.getThumbNailUrl();
        this.designer = poster.getDesigner();
        this.year = poster.getYear();
        this.createdAt = poster.getCreatedAt();
    }
}
