package geulsam.archive.domain.user.dto.req;

import geulsam.archive.domain.user.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LevelUpdateReq {
    private Level level;
    private Integer userId;
}
