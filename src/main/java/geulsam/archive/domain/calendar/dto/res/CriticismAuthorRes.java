package geulsam.archive.domain.calendar.dto.res;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismAuthor.entity.Condition;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CriticismAuthorRes {
    private int id;
    private int criticismAuthorId;
    private int order;
    private String userName;
    private Genre genre;
    private Condition condition;

    public void ChangeAuthor(CriticismAuthor criticismAuthor){
        this.criticismAuthorId = criticismAuthor.getId();
        this.userName = criticismAuthor.getAuthor().getName();
        this.genre = criticismAuthor.getGenre();
        this.condition = criticismAuthor.getCondition();
    }

    CriticismAuthorRes(int id, int order){
        this.id = id;
        this.order = order;
        this.criticismAuthorId = 0;
        this.userName = null;
        this.genre = null;
        this.condition = null;
    }
}
