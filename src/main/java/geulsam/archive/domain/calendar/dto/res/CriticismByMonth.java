package geulsam.archive.domain.calendar.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CriticismByMonth {
    private int id;
    // 합평회가 있는 달
    private int month;
    // 해당 달에 있는 합평회 목록
    private List<CriticismRes> criticismRes;

    public CriticismByMonth(int id, int month){
        this.id = id;
        this.month = month;
        this.criticismRes = new ArrayList<>();
    }
}
