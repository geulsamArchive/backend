package geulsam.archive.domain.calendar.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class RegularCriticismUploadReq {
    @Schema(description = "RegularCriticism 시작 날짜", example = "2024-09-01")
    private LocalDate start;

    @Schema(description = "RegularCriticism 끝 날짜", example = "2024-12-15")
    private LocalDate end;

    @Schema(description = "RegularCriticism 요일", example = "[\"MONDAY\", \"FRIDAY\"]")
    private List<DayOfWeek> weeks = new ArrayList<>();

    @Schema(description = "RegularCriticism 첫 번째 시작시간", example = "18:00")
    private LocalTime firstTime;

    @Schema(description = "RegularCriticism 두 번째 시작시간", example = "20:00")
    private LocalTime secondTime;

    @Schema(description = "RegularCriticism 장소", example = "Conference Room A")
    private String location;

    @AssertTrue(message = "시작 시간은 종료 시간보다 앞이어야 합니다.")
    private boolean isValidStartBeforeEnd() {
        return start.isBefore(end);
    }
}
