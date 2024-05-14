package geulsam.archive.domain.book.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookIdRes {
    /**BookId Response 객체 인덱스*/
    @Schema(example = "1")
    private String id;
    /**Book 이 공개된 날짜*/
    @Schema(example = "2024-05-06")
    private LocalDate release;
    /**Book 의 Designer*/
    @Schema(example = "김철수")
    private String Designer;
    /**Book 의 판형*/
    @Schema(example = "A4")
    private String plate;
    /**Book 의 page*/
    @Schema(example = "324")
    private int page;
    /**Book 이 저장된 URL*/
    @Schema(example = "https://sadfjdsaf")
    private String url;
}
