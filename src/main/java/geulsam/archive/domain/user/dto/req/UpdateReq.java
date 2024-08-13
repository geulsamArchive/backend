package geulsam.archive.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateReq {
    @Schema(description = "이름", example = "김철수", type = "string")
    private String name;

    @Schema(description = "이메일", example = "example@gmail.com", type = "string")
    private String email;

    @Schema(description = "user 생일")
    private LocalDate birthDay;

    @Schema(description = "user 자기소개", example = "자기소개", type = "string")
    private String introduce;

    @Schema(description = "poster 가 DB 에 insert 된 날짜", example = "가나, 다라, 마바", type = "string")
    private String keyword;

    @Schema(description = "user 핸드폰번호", example = "000-0000-0000", type = "string")
    private String phone;

    @Schema(description = "user 대표작 Id", example = "1a-2b-3c-4d", type = "string")
    private String majorWorkId;

    @Schema(description = "user 가입연도", example = "2024", type = "int")
    private int joinedAt;
}
