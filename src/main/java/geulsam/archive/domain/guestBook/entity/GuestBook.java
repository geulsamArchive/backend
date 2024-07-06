package geulsam.archive.domain.guestBook.entity;

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
public class GuestBook {
    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Integer id;

    /**방명록 작성자
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_writer_id")
    private User writer;

    /**방명록 대상자
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_owner_id")
    private User owner;

    /**방명록 작성일자
     * 타입: LocalDate
     */
    @Column(name = "guestbook_created_at")
    private LocalDate createdAt;

    /**방명록 내용
     * 타입: String
     */
    @Column(name = "guestbook_writing")
    private String writing;

    public GuestBook(User writer, User owner, LocalDate createdAt, String writing) {
        this.writer = writer;
        this.owner = owner;
        this.createdAt = createdAt;
        this.writing = writing;
    }
}
