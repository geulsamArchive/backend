package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "^[A-Z][0-9]{6}$",
            message = "학번 첫자는 알파벳 대문자, 나머지는 숫자 6자로 총 7자여야 합니다 (e.g., B123456)")
    @Schema(example = "B123456")
    private String schoolNum;

//    /**전화번호*/
//    @Schema(example = "000-0000-0000")
//    private String phone;

//    /**이메일*/
//    @NotBlank
//    @Schema(example = "example@gmail.com")
//    private String email;

    @NotNull
    @Schema(example = "2021")
    private Year joinedAt;

    @NotBlank
    @Schema(example = "자기소개입니다")
    private String introduce;

    @NotEmpty // 추후 각각 유효성 검사 필요!
    @Schema(example = "[\"가나\",\"다라\",\"마바\"]")
    private List<String> keyword;

//    @Schema(example = "2024-05-01")
//    private LocalDate birthDay;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~`!@#$%^*()\\-_+=]{8,}$",
            message = "비밀번호는 문자와 숫자를 하나 이상 포함하고 8자리 이상이어야 합니다."
    )
    @Schema(example="B123456")
    private String password;
}
