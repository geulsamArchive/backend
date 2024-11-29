package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordReq {
    // 비밀번호를 리셋할 사용자 아이디
    @NotNull
    @Schema(example="1")
    private Integer userId;

    // 사용자에게 리셋된 비밀번호를 보내 줄 메일 주소
    @Email(message = "유효한 메일 형식이 아님")
    @Schema(example="example@gmail.com")
    private String email;
}
