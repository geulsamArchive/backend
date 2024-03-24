package geulsam.archive.domain.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRes {

    /** accessToken: 사용자 식별에 사용. 유효기간 짧음*/
    private String AccessToken;
    /** refreshToken: 토큰 재발급에 사용. 유효기간 김*/
    private String RefreshToken;
}
