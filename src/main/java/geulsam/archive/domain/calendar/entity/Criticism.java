package geulsam.archive.domain.calendar.entity;

import geulsam.archive.domain.calendar.dto.req.CalendarUpdateReq;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CRITICISM")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Criticism extends Calendar{

    /**합평회 참여자
     * 타입: String
     */
    @Column(name = "criticism_participant")
    private String participant;

    /**합평회 출품자 수
     * 타입: Integer
     */
    @Column(name = "criticism_author_count")
    private Integer authorCnt;

    /**합평회 상세 코멘트
     * 타입: varchar(256)
     */
    @Column(name = "criticism_comment", length = 256)
    private String comment;

    @OneToMany(mappedBy = "criticism", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CriticismAuthor> criticismAuthors = new ArrayList<>();

    /**
     * Calendar 조회에서 Calendar 를  Criticism 으로 캐스팅 할 때 사용
     * @param calendar
     */
    public Criticism(Calendar calendar){
        super(calendar.getTitle(), calendar.getStart(), calendar.getEnd(), calendar.getLocate(), calendar.getIntroduce());
        this.setId(calendar.getId());
        this.participant = null;
        this.authorCnt = null;
        this.comment = null;
        this.criticismAuthors = null;
    }

    public Criticism(String title, LocalDateTime start, LocalDateTime end, String locate, String introduce, int authorCnt) {
        super(title, start, end, locate, introduce);
        this.authorCnt = authorCnt;
    }

    public void addCriticismAuthor(CriticismAuthor criticismAuthor) {
        criticismAuthors.add(criticismAuthor);
    }

    public void removeCriticismAuthor(CriticismAuthor criticismAuthor){
        criticismAuthors.remove(criticismAuthor);
    }

    public void updateByCalendarUpdateReq(CalendarUpdateReq calendarUpdateReq) {
        super.updateByCalendarUpdateReq(calendarUpdateReq);
    }
}
