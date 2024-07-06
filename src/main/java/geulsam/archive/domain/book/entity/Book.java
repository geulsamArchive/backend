package geulsam.archive.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    /**기본키
     * 생성 전략: UUID 자동 생성
     * 타입: UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id")
    private UUID id;

    /**표지 이미지 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "book_cover_url", length = 256)
    private String coverUrl;

    /**표지 이미지 썸네일 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "book_thumbnail_url", length = 256)
    private String thumbNailUrl;

    /**표지 제작자
     * 타입: varchar(10)
     */
    @Column(name = "book_designer", length = 10)
    private String designer;

    /**판형
     * 타입: varchar(10)
     */
    @Column(name = "book_plate", length = 10)
    private String plate;

    /**총 쪽수
     * 타입: Integer
     */
    @Column(name = "book_page_number")
    private Integer pageNumber;

    /**문집 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "book_url", length = 256)
    private String url;

    /**문집 연도
     * 타입: Year
     */
    @Column(name = "book_year")
    private Year year;

    /**문집 발행일
     * 타입: LocalDate
     */
    @Column(name ="book_release")
    private LocalDate release;

    /**문집 게시일
     * 타입: date
     */
    @Column(name = "book_created_at")
    private LocalDateTime createdAt;

    /**문집 제목
     * 타입: varchar(100)
     */
    @Column(name = "book_title", length = 100)
    private String title;

    public Book(String designer, String plate, Integer pageNumber, Year year, LocalDate release, String title) {
        this.designer = designer;
        this.plate = plate;
        this.pageNumber = pageNumber;
        this.year = year;
        this.release = release;
        this.createdAt = LocalDateTime.now();
        this.title = title;
    }

    public void saveS3publicUrl(String url, String bookCoverUrl) {
        this.url = url;
        this.coverUrl = bookCoverUrl;
    }
}
