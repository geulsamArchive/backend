package geulsam.archive.domain.user.dto.res;


import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
public class UserOneRes {
    @Schema(description = "user 이름", example = "김철수", type = "string")
    private String name;

    @Schema(description = "user 학번", example = "B000000", type = "string")
    private String schoolNum;

    @Schema(description = "user 이메일", example = "example@gmail.com", type = "string")
    private String email;
    @Schema(description = "user 생일", example = "2024년 5월 6일", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일")
    private LocalDate birthDay;
    @Schema(description = "user 자기소개", example = "자기소개", type = "string")
    private String introduce;
    @Schema(description = "poster 가 DB 에 insert 된 날짜", example = "가나, 다라, 마바", type = "string")
    private List<String> keyword;

    @Schema(description = "user 핸드폰번호", example = "000-0000-0000", type = "string")
    private String phone;

    @Schema(description = "user 대표작 Id", example = "1a-2b-3c-4d", type = "string")
    private String majorWorkId;

    @Schema(description = "user 대표 Content 이름", example = "대표작", type = "string")
    private String majorWorkName;

    @Schema(description = "user 가입연도", example = "2024", type = "int")
    private Year joinedAt;

    public UserOneRes(User user){
        this.name = user.getName();
        this.schoolNum = user.getSchoolNum();
        this.email = user.getEmail();
        this.birthDay = user.getBirthDay();
        this.introduce = user.getIntroduce();
        this.keyword = Arrays.stream(user.getKeyword().split(",")).toList();
        this.phone = user.getPhone();
        this.majorWorkId = user.getMajorWork() != null ? user.getMajorWork().getId().toString() : null;
        this.majorWorkName = user.getMajorWork() != null ? user.getMajorWork().getName() : null;
        this.joinedAt = user.getJoinedAt();
    }
}
