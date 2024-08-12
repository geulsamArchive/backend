package geulsam.archive.domain.calendar.entity;

import geulsam.archive.domain.calendar.dto.req.CalendarUpdateReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@DiscriminatorValue("CALENDAR")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
     * 타입: LocalDateTime
     */
    @Column(name = "calendar_start")
    private LocalDateTime start;

    /**종료 일시
     * 타입: LocalDateTime
     */
    @Column(name = "calendar_end")
    private LocalDateTime end;

    /** 장소
     * 타입: varchar(20)
     */
    @Column(name = "calendar_locate", length = 20)
    private String locate;

    /**소개
     * 타입: varchar(100)
     */
    @Column(name = "calendar_introduce", length =100)
    private String introduce;

    public Calendar(String title, LocalDateTime start, LocalDateTime end, String locate, String introduce) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.locate = locate;
        this.introduce = introduce;
    }

    public void updateByCalendarUpdateReq(CalendarUpdateReq calendarUpdateReq) {
        this.end = calendarUpdateReq.getEnd() != null ? calendarUpdateReq.getEnd() : this.end;
        this.introduce = calendarUpdateReq.getIntroduce() != null ? calendarUpdateReq.getIntroduce() : this.introduce;
        this.start = calendarUpdateReq.getStart() != null ? calendarUpdateReq.getStart() : this.start;
        this.title = calendarUpdateReq.getTitle() != null ? calendarUpdateReq.getTitle() : this.title;
        this.locate = calendarUpdateReq.getLocate() != null ? calendarUpdateReq.getLocate() : this.locate;
    }
}
