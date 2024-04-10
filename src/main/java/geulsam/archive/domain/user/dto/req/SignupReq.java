package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

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
}
