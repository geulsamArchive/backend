package geulsam.archive.domain.calendar.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CriticismUploadReq extends CalendarUploadReq{
    @Schema(example = "1", description = "합평회 참가자 수")
    @Positive
    private int authorCnt;
}
