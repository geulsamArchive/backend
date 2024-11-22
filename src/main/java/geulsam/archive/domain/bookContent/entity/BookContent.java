package geulsam.archive.domain.bookContent.entity;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.bookContent.dto.req.UpdateReq;
import geulsam.archive.domain.bookContent.dto.req.UploadReq;
import geulsam.archive.domain.content.entity.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookContent {

    /**기본키
     * 생성 전략: UUID 자동 생성
     * 타입: UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_content_id")
    private UUID id;

    /** 해당 작품이 있는 페이지
     * 타입: Integer
     */
    @Column(name = "book_content_page")
    private Integer page;

    /** 해당 작품 제목
     * 타입: String
     */
    @Column(name = "book_content_title")
    private String title;

    /** 해당 작품 작가
     * 타입: String
     */
    @Column(name = "book_content_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Content content;

    public BookContent(UploadReq uploadReq, Book book, Content content){
        this.page = uploadReq.getPage();
        this.title = uploadReq.getTitle();
        this.name = uploadReq.getName();
        this.book = book;
        this.content = content;
    }

    public BookContent(UpdateReq updateReq, Book book, Content content){
        this.content = content;
        this.book = book;
        this.title = updateReq.getTitle();
        this.page = updateReq.getPage();
        this.name = updateReq.getName();
    }

    public void updateByUpdateReq(UpdateReq updateReq, Content content){
        this.content = content;
        this.title = updateReq.getTitle();
        this.page = updateReq.getPage();
        this.name = updateReq.getName();
    }

}


