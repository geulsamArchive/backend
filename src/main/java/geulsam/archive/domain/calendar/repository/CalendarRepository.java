package geulsam.archive.domain.calendar.repository;

import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    @Query("SELECT COUNT(c) > 0 FROM Calendar c WHERE " + "(c.start < :end AND c.end > :start)")
    boolean existsOverlappingSchedule(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT c FROM Calendar c WHERE " +
            "(c.start BETWEEN :start AND :end) OR " +
            "(c.end BETWEEN :start AND :end) OR " +
            "(c.start <= :start AND c.end >= :end)")
    List<Calendar> calendarBySemester(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
