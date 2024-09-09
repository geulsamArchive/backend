package geulsam.archive.domain.criticismLog.dto.res;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismLog.entity.CriticismLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CriticismLogRes {
    @Schema(example = "1")
    private Integer id;
    @Schema(example = "aaa-bbb-ccc-ddd")
    private String criticismLogId;
    @Schema(example = "https://clovernote.url")
    private String cloverNoteUrl;
    @Schema(example = "1q2w3e4r")
    private String cloverNotePassword;
    @Schema(example = "김철수")
    private String userName;
    @Schema(example = "작품제목")
    private String contentTitle;
    @Schema(example = "NOVEL,ESSAY,POEM,OTHERS")
    private Genre genre;
    @Schema(example = "")
    private LocalDate localDate;

    public CriticismLogRes(CriticismLog criticismLog, int id){
        this.id = id;
        this.criticismLogId = criticismLog.getId().toString();
        this.cloverNoteUrl = criticismLog.getCloverNoteUrl();
        this.cloverNotePassword = criticismLog.getCloverNotePassword();
        this.userName = criticismLog.getUserName();
        this.contentTitle = criticismLog.getContentTitle();
        this.genre = criticismLog.getGenre();
        this.localDate = criticismLog.getLocalDate();
    }
}
