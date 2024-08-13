package geulsam.archive.domain.calendar.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CalendarUpdateReq {
    @Schema(description = "calendar 시작시간")
    private LocalDateTime start;

    @Schema(description = "calendar 끝나는 시간")
    private LocalDateTime end;

    @Schema(description = "calendar 제목", example = "소풍", type = "string")
    private String title;

    @Schema(description = "calendar 소개", example = "무슨 일정인가요", type = "string")
    private String introduce;

    @Schema(description = "calendar 진행장소", example = "G420", type = "string")
    private String locate;
}
