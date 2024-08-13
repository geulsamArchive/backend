package geulsam.archive.domain.poster.entity;

import geulsam.archive.domain.poster.dto.req.UpdateReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Poster {

    /**기본키
     * 생성 전략: UUID 자동 생성
     * 타입: UUID
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "poster_id", columnDefinition = "BINARY(16)")
    private UUID id;

    /**포스터 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "poster_url", length = 256)
    private String url;

    /**포스터 썸네일 저장주소
     * 타입: varchar(256)
     */
    @Column(name = "poster_thumbnail_url", length = 256)
    private String thumbNailUrl;

    /**포스터 연도
     * 타입: Year
     */
    @Column(name = "poster_year")
    private Year year;

    /**포스터 디자이너
     * 타입: varchar(10)
     */
    @Column(name = "poster_designer", length = 10)
    private String designer;

    /**포스터 판형
     * 타입: varchar(10)
     */
    @Column(name = "poster_plate", length = 10)
    private String plate;

    /**포스터 등록일시
     * 타입: LocalDateTime
     */
    @Column(name = "poster_created_at")
    private LocalDateTime createdAt;

    public Poster(Year year, String designer, String plate) {
        this.year = year;
        this.designer = designer;
        this.plate = plate;
        this.createdAt = LocalDateTime.now();
    }


    /*비즈니스 로직*/
    public void saveS3publicUrl(String url, String thumbNailUrl){
        this.url = url;
        this.thumbNailUrl = thumbNailUrl;
    }

    /**
     * updateReq를 받아서 Poster update
     * @param updateReq
     */
    public void updateByUpdateReq(UpdateReq updateReq) {
        this.year = updateReq.getYear() == 0 ? this.year : Year.of(updateReq.getYear());
        this.plate = updateReq.getPlate() == null ? this.plate : updateReq.getPlate();
        this.designer = updateReq.getDesigner() == null ? this.designer : updateReq.getDesigner();
    }
}
