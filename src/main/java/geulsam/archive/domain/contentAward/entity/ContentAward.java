package geulsam.archive.domain.contentAward.entity;

import geulsam.archive.domain.award.entitiy.Award;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.user.entity.User;
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
public class ContentAward {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_award_id")
    private Integer id;

    /**수상 작품
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    /**상 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_id")
    private Award award;

    /**상 시상자
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User presenter;

    /**상 수상연도
     * 타입: date
     */
    @Column(name = "content_award_at")
    private LocalDate contentAwardAt;

    /** 생성자
     */
    public ContentAward(Content content, Award award, User recipient, LocalDate contentAwardAt) {
        this.content = content;   //수정 필요
        this.award = award;
        this.presenter = recipient;
        this.contentAwardAt = contentAwardAt;
    }
}
