package geulsam.archive.domain.criticismAuthor.entity;

import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.content.entity.Content;
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
    @OneToOne
    @JoinColumn(name = "criticism_id")
    private Criticism criticism;

    /**작품 아이디
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    /**작가의 순서
     * 타입: Integer
     */
    @Column(name = "criticism_author_order")
    private Integer order;

    /**클로버노트 주소
     * 타입: varchar(100)
     */
    @Column(name = "criticism_author_cloverNoteURL")
    private String cloverNoteURL;

    /**생성자
     */
    public CriticismAuthor(User author, Criticism criticism) {
        this.author = author;
        this.criticism = criticism;
    }

}
