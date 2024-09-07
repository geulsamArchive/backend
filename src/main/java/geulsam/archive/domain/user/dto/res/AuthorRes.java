package geulsam.archive.domain.user.dto.res;

import geulsam.archive.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRes {

    private String name;
    private String introduce;
    private String[] keywords;

    public AuthorRes(User user){
        this.name = user.getName();
        this.introduce = user.getIntroduce();
        this.keywords = user.getKeyword().split(",");
    }
}
