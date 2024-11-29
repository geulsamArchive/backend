package geulsam.archive.domain.book.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.bookContent.dto.res.BookContentRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BookIdRes {
    /**BookId Response 객체 인덱스*/
    @Schema(example = "1")
    private String id;
    /**Book 이 공개된 날짜*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    @Schema(example = "2024년 5월 6일", type = "string")
    private LocalDate release;
    /**Book 의 Designer*/
    @Schema(example = "김철수")
    private String designer;
    /**Book 의 판형*/
    @Schema(example = "A4")
    private String plate;
    /**Book 의 page*/
    @Schema(example = "324")
    private int page;
    /**Book 이 저장된 URL*/
    @Schema(example = "https://sadfjdsaf")
    private String url;
    /**Book 의 제목*/
    @Schema(example = "책 제목")
    private String title;

    @Schema(example = "https://sadsaf")
    private String bookCover;

    @Schema()
    private List<BookContentRes> bookContentResList;

    /**
     * Book 객체를 받아 BookIdRes 로 매핑 
     * @param book
     */
    public BookIdRes(Book book, List<BookContentRes> bookContentResList){
        this.id = book.getId().toString();
        this.release = book.getRelease();
        this.designer = book.getDesigner();
        this.plate = book.getPlate();
        this.page = book.getPageNumber();
        this.url = book.getUrl();
        this.title =book.getTitle();
        this.bookCover = book.getCoverUrl();
        this.bookContentResList = bookContentResList;
    }
}
