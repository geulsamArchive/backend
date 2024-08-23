package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordReq {

    @Schema(example="B123456")
    private String oldPassword;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 문자와 숫자를 하나 이상 포함하고 8자리 이상이어야 합니다."
    )
    @Schema(example="B123456")
    private String newPassword;
}
