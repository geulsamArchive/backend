package geulsam.archive.domain.criticismLog.entity;

import geulsam.archive.domain.calendar.dto.req.CalendarUpdateReq;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismLog.dto.req.UploadReq;
import geulsam.archive.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CriticismLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "criticism_log_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "criticism_log_clover_url")
    private String cloverNoteUrl;

    @Column(name = "criticism_log_clover_password")
    private String cloverNotePassword;

    @Column(name = "criticism_log_user_name")
    private String userName;

    @Column(name = "criticism_log_content_title")
    private String contentTitle;

    @Column(name = "criticism_author_genre")
    private Genre genre;

    @Column(name = "criticism_log_date")
    private LocalDate localDate;

    /**
     * 클로버노트만 업데이트
     * @param cloverNoteUrl
     * @param cloverNotePassword
     */
    public void updateClover(String cloverNoteUrl, String cloverNotePassword) {
        this.cloverNoteUrl = (cloverNoteUrl != null) ? cloverNoteUrl : this.cloverNoteUrl;
        this.cloverNotePassword = (cloverNotePassword != null) ? cloverNotePassword : this.cloverNotePassword;
    }

    public CriticismLog(UploadReq uploadReq){
        this.cloverNoteUrl = uploadReq.getCloverNoteUrl();
        this.cloverNotePassword = uploadReq.getCloverNotePassword();
        this.userName = uploadReq.getUserName();
        this.contentTitle = uploadReq.getContentTitle();
        this.genre = uploadReq.getGenre();
        this.localDate = uploadReq.getLocalDate();
    }
}
