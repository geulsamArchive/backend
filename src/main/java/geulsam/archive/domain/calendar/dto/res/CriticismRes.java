package geulsam.archive.domain.calendar.dto.res;

import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CriticismRes {
    private int id;
    private int criticismId;
    // 해당 합평회 시작 시간
    private LocalDateTime start;

    private int authorCnt;
    // 해당 합평회에 나가는 사람
    private List<CriticismAuthorRes> criticismAuthorResList;

    public CriticismRes(Criticism criticism, int id){
        this.id = id;
        this.authorCnt = criticism.getAuthorCnt() != null ? criticism.getAuthorCnt() : 0;
        this.criticismId = criticism.getId();
        this.start = criticism.getStart();
        this.criticismAuthorResList = new ArrayList<>();
        
        // AuthorCnt 만큼 반복문 실행
        for(int i = 0; i<authorCnt;i++){
            this.criticismAuthorResList.add(new CriticismAuthorRes(i, i+1));
        }

        if (criticism.getCriticismAuthors() != null) {
            criticism.getCriticismAuthors().forEach(criticismAuthor -> {
                int index = criticismAuthor.getOrder() - 1;
                if (index >= 0 && index < this.criticismAuthorResList.size()) {
                    this.criticismAuthorResList.get(index).ChangeAuthor(criticismAuthor);
                }
            });
        }
    }
}
