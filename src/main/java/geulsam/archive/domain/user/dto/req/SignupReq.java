package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
public class SignupReq {

    /**이름*/
    @NotBlank
    @Schema(example = "김철수")
    private String name;

    /**학번*/
    @NotBlank
    @Size(max = 7, min = 7)
    @Schema(example = "B123456")
    private String schoolNum;

    /**전화번호*/
    @NotBlank
    @Schema(example = "000-0000-0000")
    private String phone;

    /**이메일*/
    @NotBlank
    @Schema(example = "example@gmail.com")
    private String email;

    @NotNull
    @Schema(example = "2021")
    private Year joinedAt;

    @NotBlank
    @Schema(example = "자기소개입니다")
    private String introduce;

    @NotEmpty // 추후 각각 유효성 검사 필요!
    @Schema(example = "[\"가나\",\"다라\",\"마바\"]")
    private List<String> keyword;

    @NotNull
    @Schema(example = "2024-05-01")
    private LocalDate birthDay;
}
