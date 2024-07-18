package geulsam.archive.domain.calendar.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CalendarUploadReq {
    @Schema(example = "일정 이름")
    @NotBlank
    private String title;

    @Schema(example = "2023-04-01T12:00:00", type = "string")
    @NotNull
    private LocalDateTime start;

    @Schema(example = "2023-04-01T13:30:00", type = "string")
    @NotNull
    private LocalDateTime end;

    @Schema(example = "일정이 진행되는 장소")
    @NotBlank
    private String locate;

    @Schema(example = "일정에 대한 간단한 소개")
    private String introduce;

    @AssertTrue(message = "시작 시간은 종료 시간보다 앞이어야 합니다.")
    private boolean isValidStartBeforeEnd() {
        return start.isBefore(end);
    }
}
