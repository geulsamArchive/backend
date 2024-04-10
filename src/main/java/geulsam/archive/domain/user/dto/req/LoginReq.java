package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReq {

    /** 학번 */
    @NotBlank
    @Size(max = 7, min = 7)
    @Schema(example = "B123456")
    private String schoolNum;

    /** 비밀번호 */
    @NotBlank
    @Schema(example="B123456")
    private String password;
}
