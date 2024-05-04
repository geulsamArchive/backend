package geulsam.archive.domain.calendar.repository;

import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}
