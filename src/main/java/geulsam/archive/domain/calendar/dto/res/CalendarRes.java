package geulsam.archive.domain.calendar.dto.res;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CalendarRes {
    int id;
    String month;
    List<CalendarEvent> events;

    public CalendarRes(int id, String month){
        this.id = id;
        this.month = month;
        this.events = new ArrayList<>();
    }

    public void sortCalendarEvent(){
            events.sort(new CalendarEventComparator());
    }

    public void addId() {
        events.forEach(event -> event.setId(events.indexOf(event) + 1));
    }
}

class CalendarEventComparator implements Comparator<CalendarEvent> {
    @Override
    public int compare(CalendarEvent ce1, CalendarEvent ce2) {
        // Compare by start time (LocalDate)
        LocalDate startDate1 = ce1.getStartTime().toLocalDate();
        LocalDate startDate2 = ce2.getStartTime().toLocalDate();
        int startDateComparison = startDate1.compareTo(startDate2);
        if (startDateComparison != 0) {
            return startDateComparison;
        }

        // Compare by longTerm status (true comes before false)
        boolean isLongTerm1 = ce1.getIsLongTerm();
        boolean isLongTerm2 = ce2.getIsLongTerm();
        if (isLongTerm1 != isLongTerm2) {
            return Boolean.compare(isLongTerm2, isLongTerm1); // Reverse order: true before false
        }

        // If longTerm status is true, compare by end time (earliest first)
        if (isLongTerm1 && isLongTerm2) {
            int endDateComparison = ce1.getEndTime().compareTo(ce2.getEndTime());
            if (endDateComparison != 0) {
                return endDateComparison;
            }
        }

        // If longTerm status is false, compare by special status (true comes before false)
        boolean isCritical1 = ce1.getIsCriticism();
        boolean isCritical2 = ce2.getIsCriticism();
        return Boolean.compare(isCritical2, isCritical1); // Reverse order: true before false
    }
}