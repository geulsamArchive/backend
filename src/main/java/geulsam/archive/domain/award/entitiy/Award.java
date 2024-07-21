package geulsam.archive.domain.award.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**Award Entity**/
@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Award {

    /**기본키
     * 생성 전략: 자동 증가
     * 타입: Integer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Integer id;

    /**상 이름
     * 타입: varchar(100)
     */
    @Column(name = "award_name", length = 100)
    private String name;

    /**상 설명
     * 타입: varchar(100)
     */
    @Column(name = "award_explain", length = 100)
    private String explain;

    /**상 생성날짜
     * 타입: date
     */
    @Column(name = "award_created_at")
    private LocalDate createdAt;

    /**생성자
     */
    public Award(String name, String explain, LocalDate createdAt) {
        this.name = name;
        this.explain = explain;
        this.createdAt = createdAt != null ? createdAt : LocalDate.now();
    }

    public void changeExplain(String explain) {
        this.explain = explain;
    }
}
