package geulsam.archive.domain.user.dto.req;

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
    private String schoolNum;

    /** 비밀번호 */
    @NotBlank
    private String password;
}
