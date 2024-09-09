package geulsam.archive.domain.contentAward.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class PresentAwardReq {
    @Schema(description = "작품 아이디", example = "adsa-dsafdsa-vsadf-dsafdsa")
    @NotNull(message = "작품 아이디는 Null 일 수 없습니다")
    private UUID contentId;
    @Schema(description = "상 아이디", example = "1")
    @NotNull(message = "상 아이디는 Null 일 수 없습니다")
    private int awardId;
    @Schema(description = "상 시상자 아이디", example = "1")
    @NotNull(message = "상 시상자 아이디는 Null 일 수 없습니다")
    private int presenterId;
    @Schema(description = "수상한 날짜", example = "2024-03-10")
    @NotNull(message = "수상한 날짜는 Null 일 수 없습니다")
    private LocalDate presentAt;
}
