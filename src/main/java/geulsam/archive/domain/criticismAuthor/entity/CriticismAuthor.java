package geulsam.archive.domain.criticismAuthor.entity;

import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticismAuthor {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criticism_author_id")
    private Integer id;

    /**작가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    /**합평회 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criticism_id")
    private Criticism criticism;

    /**작가의 순서
     * 타입: Integer
     */
    @Column(name = "criticism_author_order")
    private Integer order;

    /** 작품 장르
     * 타입 : Genre ENUM
     */
    @Column(name = "criticism_author_genre")
    private Genre genre;

    /** 승인 현황
     * 타입 : Condition ENUM(FIXED, UNFIXED)
     */
    @Column(name = "criticism_author_condition")
    private Condition condition;

    /**생성자
     */
    public CriticismAuthor(User author, Criticism criticism, int order, Genre genre) {
        this.author = author;
        this.criticism = criticism;
        this.order = order;
        this.genre = genre;
        this.condition = Condition.UNFIXED;
    }

    /**
     * 신청을 고정-고정해제 하는 함수
     */
    public void toggleCondition() {
        if(this.condition == Condition.UNFIXED){
            this.condition = Condition.FIXED;
        } else {
            this.condition = Condition.UNFIXED;
        }
    }

//    public void close(CriticismAuthorCloseReq criticismAuthorCloseReq) {
//        //criticismAuthorCloseReq.getCloverNotePassword();
//        this.cloverNoteURL = criticismAuthorCloseReq.getCloverNoteURL();
//    }
}
