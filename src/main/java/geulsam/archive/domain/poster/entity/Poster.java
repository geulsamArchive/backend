package geulsam.archive.domain.poster.entity;

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
public class Poster {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poster_id")
    private Integer id;

    /**포스터 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "poster_url", length = 256)
    private String url;

    /**게시일
     * 타입: date
     */
    @Column(name = "poster_date")
    private LocalDate date;

    /**제작자
     * 타입: varchar(10)
     */
    @Column(name = "poster_creater", length = 10)
    private String creater;

    /**판형
     * 타입: varchar(10)
     */
    @Column(name = "poster_plate", length = 10)
    private String plate;

    public Poster(String url, LocalDate date, String creater, String plate) {
        this.url = url;
        this.date = date;
        this.creater = creater;
        this.plate = plate;
    }
}
