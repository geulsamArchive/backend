package geulsam.archive.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRes {

    /** accessToken: 사용자 식별에 사용. 유효기간 짧음*/
    @Schema(description = "accessToken:사용자 식별에 사용 유효기간 짧음", example = "asdfdsa.sadfa.dsafdsa")
    private String AccessToken;
    /** refreshToken: 토큰 재발급에 사용. 유효기간 김*/
    @Schema(description = "refreshToken: 토큰 재발급에 사용 유효기간 김", example = "sadf.dsafd.asdf")
    private String RefreshToken;
}
