package geulsam.archive.domain.calendar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    public Criticism(String title, LocalDate date, String introduce, String participant, Integer authorCnt, String comment) {
        super(title, date, introduce);
        this.participant = participant;
        this.authorCnt = authorCnt;
        this.comment = comment;
    }
}
