package geulsam.archive.domain.content.entity;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    /**기본키
     * 생성 전략: UUID 자동 생성
     * 타입: UUID
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", columnDefinition = "BINARY(16)")
    private UUID id;

    /**콘텐츠 작성자
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**연관된 문집
     * 타입: Book
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /**콘텐츠 이름
     * 타입: varchar(100)
     */
    @Column(name = "content_name", length = 100, nullable = false)
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
    @Column(name = "content_created_at", nullable = false)
    private LocalDateTime createdAt;

    /**콘텐츠 공개여부
     * 타입: Enum(PRIVATE,LOGGEDIN,EVERY)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "content_is_visible")
    private IsVisible isVisible;

    /**콘텐츠 조회수
     * 타입: Integer
     * 초기값: 0
     */
    @Column(name = "content_view_count")
    private Integer viewCount;

    /**문집에 속한 페이지
     * 타입: Integer
     */
    @Column(name = "content_book_page")
    private Integer bookPage;

    /**콘텐츠 소개
     * 타입: varchar(255)
     */
    @Column(name = "content_sentence", length = 256)
    private String sentence;

    /**
     * 생성자
     */
    public Content(User user, Book book, String name, Genre genre, LocalDateTime createdAt, IsVisible isVisible, Integer bookPage, String sentence){
        this.user = Objects.requireNonNull(user, "User cannot be null");
        this.book = book;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.genre = genre != null ? genre : Genre.OTHERS;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.isVisible = isVisible != null ? isVisible : IsVisible.PRIVATE;
        this.viewCount = 0;
        this.bookPage = bookPage;
        this.sentence = sentence;
    }

    public void saveS3publicUrl(String pdfUrl, String htmlUrl) {
        this.pdfUrl = pdfUrl;
        this.htmlUrl = htmlUrl;
    }

    public void updateContent(String newName, String newPdfUrl, String newHtmlUrl, Genre newGenre, String newSentence, IsVisible newIsVisible) {
        this.name = newName;
        this.pdfUrl = newPdfUrl;
        this.htmlUrl = newHtmlUrl;
        this.genre = newGenre;
        this.sentence = newSentence;
        this.isVisible = newIsVisible;
    }

    public void changeAuthor(User newUser) {
        if(!this.user.equals(newUser)) {
            this.user = Objects.requireNonNull(newUser, "User cannot be null");
        }
    }

    public void changeName(String newName) { this.name = newName; }
    public void changePdfUrl(String newPdfUrl) {
        this.pdfUrl = newPdfUrl;
    }

    public void changeHtmlUrl(String newHtmlUrl) {
        this.htmlUrl = newHtmlUrl;
    }

    public void changeGenre(Genre genre) { this.genre = genre; }

    public void changeIsVisible(IsVisible isVisible) {
        this.isVisible = isVisible;
    }

    public void changeBook(Book newBook) {
        this.book = newBook;
    }

    /**
     * 콘텐츠의 페이지 번호 변경 메서드
     * 페이지 번호는 0 이상이어야 합니다.
     */
    public void changeBookPage(Integer newBookPage) {
        if (newBookPage != null && newBookPage < 0) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "Page number must be non-negative");
        }

        if(!Objects.equals(this.bookPage, newBookPage)) {
            this.bookPage = newBookPage;
        }
    }

    public void changeSentence(String sentence) { this.sentence = sentence; }

    public void moveToBook(Book newBook, Integer newBookPage) {
        this.book = newBook;
        this.bookPage = newBookPage;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void resetViewCount() {
        this.viewCount = 0;
    }
}
