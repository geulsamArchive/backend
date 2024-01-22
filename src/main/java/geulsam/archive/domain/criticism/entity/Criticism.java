package geulsam.archive.domain.criticism.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**Criticism Entity*/
@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Criticism {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criticism_id")
    private Integer id;

    /**합평회 행사 일시
     * 타입: datetime(6)
     */
    @Column(name = "criticism_at")
    private LocalDateTime criticismAt;

    /**합평회 테이블 생성 일시
     * 타입: datetime(6)
     */
    @Column(name = "criticism_created_at")
    private LocalDateTime createdAt;

    /**합평회 참여자
     * 타입: String
     */
    @Column(name = "criticism_participant")
    private String pariticiant;

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

    /**생성자
     * NOT NULL 이어야 하는 값들을 인자로 받음
     */
    public Criticism(LocalDateTime criticismAt, LocalDateTime createdAt) {
        this.criticismAt = criticismAt;
        this.createdAt = createdAt;
    }
}
