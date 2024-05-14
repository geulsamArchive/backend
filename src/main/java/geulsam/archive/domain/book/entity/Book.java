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
     * 생성 전략: 자동 증가
     * 타입: Integer
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

    /**문집저장주소
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

    /**문집게시일
     * 타입: date
     */
    @Column(name = "book_created_at")
    private LocalDateTime createdAt;




    public Book(String coverUrl, String designer, String plate, Integer pageNumber, Year year, String url, LocalDate release, LocalDateTime createdAt) {
        this.coverUrl = coverUrl;
        this.designer = designer;
        this.plate = plate;
        this.pageNumber = pageNumber;
        this.url = url;
        this.year = year;
        this.release = release;
        this.createdAt = createdAt;
    }
}
