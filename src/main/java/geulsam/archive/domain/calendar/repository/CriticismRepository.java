package geulsam.archive.domain.calendar.repository;

import geulsam.archive.domain.calendar.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CriticismRepository extends JpaRepository<Criticism, Integer> {
//    @Query("SELECT COUNT(c) > 0 FROM Criticism c WHERE " + "(c.start < :end AND c.end > :start)")
//    boolean existsOverlappingSchedule(@Param("startTime") LocalDateTime start,
//                                      @Param("endTime") LocalDateTime end);
}
