package geulsam.archive.domain.content.entity;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**Content Entity*/
@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer id;

    /** 콘텐츠 작성자
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**콘텐츠 이름
     * 타입: varchar(100)
     */
    @Column(name = "content_name", length = 100)
    private String name;

    /**콘텐츠 저장 url
     * 타입: varchar(255)
     */
    @Column(name = "content_url")
    private String url;

    /**콘텐츠 장르
     * 타입: Enum(NOVEL,ESSAY,POEM,OTHERS)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "content_genre")
    private Genre genre;

    /**콘텐츠 등록일시
     * 타입: datetime(6)
     */
    @Column(name = "content_created_at")
    private LocalDateTime createdAt;

    /**콘텐츠 공개여부
     * 타입: Enum(PRIVATE,LOGGEDIN,EVERY)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "content_is_visible")
    private IsVisible isVisible;

    /**콘텐츠 조회수
     * 타입: Integer
     */
    @Column(name = "content_view_count")
    private Integer viewCount;

//    @OneToOne
//    @JoinColumn(name = "book_id")
//    private Book book;

    /**문집에 속한 페이지
     * 타입: Integer
     */
    @Column(name = "content_book_page")
    private Integer bookPage;

    /**Content-Comment 양방향 매핑
     * 콘텐츠에 작성된 코멘트 list
     */
//    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();

    /**생성자
     * NOT NULL 이어야 하는 값들을 인자로 받음
     */
    public Content(User user, String name, Genre genre, LocalDateTime createdAt, IsVisible isVisible){
        /*user-content 연관관계 설정*/
        this.user = user;
//        user.getContents().add(this);

        this.name = name;
        this.genre = genre;
        this.createdAt = createdAt;
        this.isVisible = isVisible;
        this.viewCount = 0;
    }
}
