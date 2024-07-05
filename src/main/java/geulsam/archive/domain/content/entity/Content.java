package geulsam.archive.domain.content.entity;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    /**기본키
     * 생성 전략: UUID 자동 생성
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", columnDefinition = "BINARY(16)")
    private UUID id;

    /**콘텐츠 작성자
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**문집 아이디
     * 타입: Book
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /**콘텐츠 이름
     * 타입: varchar(100)
     */
    @Column(name = "content_name", length = 100)
    private String name;

    /**pdf 콘텐츠 저장 url
     * 타입: varchar(255)
     */
    @Column(name = "content_pdf_url", length = 256)
    private String pdfUrl;

    /**html 콘텐츠 저장 url
     * 타입: varchar(255)
     */
    @Column(name = "content_html_url", length = 256)
    private String htmlUrl;

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

    /**문집에 속한 페이지
     * 타입: Integer
     */
    @Column(name = "content_book_page")
    private Integer bookPage;

    /**콘텐프 소개
     * 타입: varchar(255)
     */
    @Column(name = "content_sentence", length = 256)
    private String sentence;

    /**Content-Comment 양방향 매핑
     * 콘텐츠에 작성된 코멘트 list
     */
//    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();

    public Content(User user, Book book, String name, String pdfUrl, Genre genre, LocalDateTime createdAt, IsVisible isVisible, String sentence){
        this.id = UUID.randomUUID();
        this.user = user;
        this.book = book;
        /*user-content 연관관계 설정*/
//        user.getContents().add(this);
        this.name = name;
        this.pdfUrl = pdfUrl;
        this.genre = genre;
        this.createdAt = createdAt;
        this.isVisible = isVisible;
        this.viewCount = 0;
        this.sentence = sentence;
    }
}
