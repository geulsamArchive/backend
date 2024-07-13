package geulsam.archive.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CheckRes {

    @Schema(description = "유저 아이디", example = "1")
    private int id;
    @Schema(description = "유저 역할 목록", example = "[\"ADMIN\",\"NORMAL\",\"GRADUATED\",\"SUSPENDED\"]")
    private List<String> roles;
}
