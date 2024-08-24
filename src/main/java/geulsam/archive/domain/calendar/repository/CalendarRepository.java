package geulsam.archive.domain.calendar.repository;

import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    @Query("SELECT COUNT(c) > 0 FROM Calendar c WHERE " + "(c.start < :end AND c.end > :start)")
    boolean existsOverlappingSchedule(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
