package geulsam.archive.domain.comment.entity;

import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**Comment Entity*/
@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    /**댓글 내용
     * 타입: varchar(255)
     * */
    @Column(name = "comment_writing")
    private String writing;

    /**댓글 작성일시
     * 타입: datetime(6)
     */
    @Column(name = "comment_created_at")
    private LocalDateTime createdAt;

    /**댓글 작성한 유저
     * 타입: User
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** 댓글 작성된 콘텐츠
     * 타입: Content
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    /**생성자
     * NOT NULL 이어야 하는 값들을 인자로 받음
     */
    public Comment(String writing, LocalDateTime createdAt, User user, Content content){
        this.writing = writing;
        this.createdAt = createdAt;

        /*user-comment 연관관계 설정*/
        this.user = user;
//        user.getComments().add(this);

        /*content-comment 연관관계 설정*/
        this.content = content;
//        content.getComments().add(this);
    }
}
