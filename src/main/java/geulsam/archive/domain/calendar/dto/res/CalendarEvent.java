package geulsam.archive.domain.calendar.dto.res;

import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CalendarEvent {
    private int eventId;
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String introduce;
    private String locate;
    private List<String> userAndGenre = new ArrayList<>();
    private Boolean isCriticism;
    private Boolean isLongTerm;


    public CalendarEvent(Criticism criticism) {
        this.eventId = criticism.getId();
        this.startTime = criticism.getStart();
        this.endTime = criticism.getEnd();
        this.title = criticism.getTitle();
        this.introduce = criticism.getIntroduce();
        this.locate = criticism.getLocate();
        this.isCriticism = (criticism.getAuthorCnt() != null);
        this.isLongTerm = !criticism.getStart().toLocalDate().equals(criticism.getEnd().toLocalDate());
        // 발표 순서대로 정렬
        if (criticism.getCriticismAuthors() != null) {
            criticism.getCriticismAuthors().stream()
                    .sorted(Comparator.comparingInt(CriticismAuthor::getOrder))
                    .forEach(criticismAuthor ->
                            userAndGenre.add(criticismAuthor.getOrder().toString() + ' ' + criticismAuthor.getAuthor().getName() + '(' + criticismAuthor.getGenre().toString() + ')')
                    );
        }
    }
}
