package geulsam.archive.domain.book.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class BookRes {
    /**Book Response 객체 인덱스*/
    @Schema(example = "1")
    private int id;
    /**bookCover 저장된 url*/
    @Schema(example = "https://bookCoverUrl")
    private String bookCover;
    /**Book 이 제작된 연도*/
    @Schema(example = "2021")
    private int year;
    /**Book 에 대한 설명*/
    @Schema(example = "문집 설명")
    private String description;
    /**Book 객체의 PK*/
    @Schema(example = "afdsa-savdsaf-dsfasfsa-vdsas")
    private String bookId;
    /**Book 객체가 저장된 시간*/
    @Schema(example = "2024-04-11T:12:30:11")
    private LocalDateTime createdAt;
}
