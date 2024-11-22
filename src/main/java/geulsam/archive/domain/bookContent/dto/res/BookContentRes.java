package geulsam.archive.domain.bookContent.dto.res;

import geulsam.archive.domain.bookContent.entity.BookContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookContentRes {

    /**리스트에서의 아이디*/
    @Schema(example = "1")
    private int id;
    /**문집의 목차 요소의 id - 페이지 연결에 사용**/
    @Schema(example = "eee-ddd-fff")
    private String bookContentId;
    /**문집에 있는 작품의 id - 페이지 연결에 사용**/
    @Schema(example = "aaa-bbb-ccc-ddd")
    private String contentId;
    /**문집에 있는 작품의 제목**/
    @Schema(example = "작품의 제목")
    private String title;
    /**문집에 있는 작품의 작성자*/
    @Schema(example = "김철수")
    private String name;
    /**문집에 있는 작품의 페이지**/
    @Schema(example = "68")
    private Integer page;

    public BookContentRes(BookContent bookContent, int id) {
        this.id = id;
        this.bookContentId = bookContent.getId().toString();
        this.contentId = (bookContent.getContent() != null) ? bookContent.getContent().getId().toString() : null;
        this.title = bookContent.getTitle();
        this.name = bookContent.getName();
        this.page = bookContent.getPage();
    }
}
