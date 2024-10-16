package geulsam.archive.domain.user.entity;

import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.user.dto.req.UpdateReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**User Entity*/
@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    /**유저 이름
     * 타입: varchar(10)
     */
    @Column(name = "user_name", length = 10)
    private String name;

    /**유저 비밀번호
     * 타입: varchar(255)
     * 암호화 필수
     */
    @Column(name = "user_password", length = 256)
    private String password;

    /**유저 학번
     * 타입: varchar(10)
     */
    @Column(name = "user_school_num", length = 10)
    private String schoolNum;

    /**유저 별명
     * 타입: varchar(30)
     */
    @Column(name = "user_nickname", length = 30)
    private String nickname;

    /**유저 등급
     * 타입: Enum(ADMIN,NORMAL,GRADUATED,SUSPENDED)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_level")
    private Level level;

    /**유저 전화번호
     * 타입: varchar(30)
     */
    @Column(name = "user_phone", length = 30)
    private String phone;

    /** 유저 가입일시
     * 타입: datetime(6)
     */
    @Column(name = "user_created_at")
    private LocalDateTime createdAt;

    /** 유저 대표작
     * 타입: Content
     */
    @OneToOne
    @JoinColumn(name = "content_id")
    private Content majorWork;

    /** 유저 자기소개
     * 타입: varchar(128)
     */
    @Column(name = "user_introduce", length = 128)
    private String introduce;

    /** 유저 동아리 가입 연도
     * 타입: Year
     */
    @Column(name = "user_joined_at")
    private Year joinedAt;

    /**유저 이메일
     * 타입: varchar(100)
     */
    @Column(name = "user_email", length = 100)
    private String email;

    /**유저 키워드
     * 타입: varchar(100)
     */
    @Column(name = "user_keyword", length = 100)
    private String keyword;

    /** 유저 생일
     * 타입: datetime(6)
     */
    @Column(name = "user_birth_day")
    private LocalDate birthDay;

    /**User-Content 양방향 매핑
     * 유저가 작성한 content list
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contents = new ArrayList<>();

    /**User-Comment 양방향 매핑
     * 유저가 작성한 comment list
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**생성자
     * NOT NULL 이어야 하는 값들을 인자로 받음
     */
    public User(String name,
                String password,
                String schoolNum,
                Level level,
                LocalDateTime createdAt,
                String email,
                String phone,
                Year joinedAt,
                String introduce,
                String keyword,
                LocalDate birthDay){
        this.name = name;
        this.password = password;
        this.schoolNum = schoolNum;
        this.level = level;
        this.createdAt = createdAt;
        this.email = email;
        this.phone = phone;
        this.joinedAt = joinedAt;
        this.introduce = introduce;
        this.keyword = keyword;
        this.birthDay = birthDay;
    }

    //이 밑에 앞으로 필요한 비즈니스 로직 작성

    public void updateByPutReq(UpdateReq updateReq) {
        this.schoolNum = updateReq.getSchoolNum() == null ? this.name : updateReq.getSchoolNum();
        this.name = updateReq.getName() == null ? this.name : updateReq.getName();
        this.email = updateReq.getEmail() == null ? this.email : updateReq.getEmail();
        this.phone = updateReq.getPhone() == null ? this.phone : updateReq.getPhone();
        this.joinedAt = updateReq.getJoinedAt() == 0 ? this.joinedAt : Year.of(updateReq.getJoinedAt());
        this.keyword = updateReq.getKeyword() == null ? this.keyword : updateReq.getKeyword();
        this.introduce = updateReq.getIntroduce() == null ? this.introduce : updateReq.getIntroduce();
        this.birthDay = updateReq.getBirthDay() == null ? this.birthDay : updateReq.getBirthDay();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateLevel(Level level){
        this.level = level;
    }
}
