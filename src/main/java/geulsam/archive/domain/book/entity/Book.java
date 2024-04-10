package geulsam.archive.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    /**표지 이미지 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "book_cover_url", length = 256)
    private String coverUrl;

    /**표지 제작자
     * 타입: varchar(10)
     */
    @Column(name = "book_designer", length = 10)
    private String desinger;

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

    /**문집게시일
     * 타입: date
     */
    @Column(name = "book_date")
    private LocalDate date;

    public Book(String coverUrl, String desinger, String plate, Integer pageNumber, LocalDate date) {
        this.coverUrl = coverUrl;
        this.desinger = desinger;
        this.plate = plate;
        this.pageNumber = pageNumber;
        this.url = url;
        this.date = date;
    }
}
