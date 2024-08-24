package geulsam.archive.domain.criticismAuthor.dto.res;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CriticismAuthorRes {

    private int id;
    private int criticismId;
    private String contentId;
    private Genre genre;
    private String title;
    private String author;
    private LocalDate createdAt;
    private String password;
    private String cloverNoteURL;

    public CriticismAuthorRes(CriticismAuthor criticismAuthor, int id){
        this.id = id;
        this.criticismId = criticismAuthor.getCriticism().getId();
        this.contentId = (criticismAuthor.getContent() != null) ? criticismAuthor.getContent().getId().toString() : null;
        this.genre = criticismAuthor.getGenre();
        this.author = criticismAuthor.getAuthor().getName();
        this.createdAt=criticismAuthor.getCriticism().getStart().toLocalDate();
        this.password = "SORRY";
        this.cloverNoteURL = criticismAuthor.getCloverNoteURL();
    }
}
