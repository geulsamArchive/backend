package geulsam.archive.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.book.entity.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
public class BookRes {
    /**Book Response 객체 인덱스*/
    @Schema(example = "1")
    private int id;
    /**bookCover 저장된 url*/
    @Schema(example = "https://bookCoverUrl")
    private String bookCover;
    /**Book 이 제작된 연도*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년")
    @Schema(example = "2021년", type = "string")
    private Year year;
    /**Book 에 대한 설명*/
    @Schema(example = "문집 설명")
    private String description;
    /**Book 객체의 PK*/
    @Schema(example = "afdsa-savdsaf-dsfasfsa-vdsas")
    private String bookId;
    /**Book 객체가 저장된 시간*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 4월 11일", type = "string")
    private LocalDateTime createdAt;
    /**Book 객체 재목*/
    @Schema(example = "책 제목")
    private String title;

    public BookRes(Book book, int id){
        this.id = id;
        this.bookCover = book.getCoverUrl();
        this.year = book.getYear();
        this.description = null;
        this.bookId = book.getId().toString();
        this.createdAt = book.getCreatedAt();
        this.title = book.getTitle();
    }
}
