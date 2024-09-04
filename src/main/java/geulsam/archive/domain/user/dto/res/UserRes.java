package geulsam.archive.domain.user.dto.res;

import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UserRes {
    private int id;
    private Integer userId;
    private Level level;
    private String name;
    private Year joinedAt;
    private String schoolNum;
    private LocalDate birthDay;
    private String phone;
    private String email;


    public UserRes(User user, int id){
        this.id = id;
        this.userId = user.getId();
        this.level = user.getLevel();
        this.name = user.getName();
        this.joinedAt = user.getJoinedAt();
        this.schoolNum = user.getSchoolNum();
        this.birthDay  = user.getBirthDay();
        this.phone = user.getPhone();
        this.email = user.getEmail();
    }
}
