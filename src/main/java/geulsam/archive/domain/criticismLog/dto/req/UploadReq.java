package geulsam.archive.domain.criticismLog.dto.req;

import geulsam.archive.domain.content.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UploadReq {
    @Schema(description = "작품 제목", example = "비행운")
    private String contentTitle;
    @Schema(description = "합평이 시행된 시간", example = "2023-08-29")
    private LocalDate localDate;
    @Schema(description = "발표자 이름", example = "김철수")
    private String userName;
    @Schema(description = "작품 장르", example = "NOVEL,ESSAY,POEM,OTHERS")
    private Genre genre;
    @Schema(description = "클로버노트 URL", example = "https://localhost:8080/aaaa")
    private String cloverNoteUrl;
    @Schema(description = "클로버노트 비밀번호", example = "1q2w3e4r!")
    private String cloverNotePassword;
}
