package geulsam.archive.domain.calendar.entity;

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
public class Calendar {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Integer id;

    /**제목
     * 타입: varchar(30)
     */
    @Column(name = "calendar_title", length = 30)
    private String title;

    /**일시
     * 타입: date
     */
    @Column(name = "calendar_date")
    private LocalDate date;

    public Calendar(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }
}
