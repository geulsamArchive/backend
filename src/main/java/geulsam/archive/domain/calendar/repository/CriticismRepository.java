package geulsam.archive.domain.calendar.repository;

import geulsam.archive.domain.calendar.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CriticismRepository extends JpaRepository<Criticism, Integer> {
//    @Query("SELECT COUNT(c) > 0 FROM Criticism c WHERE " + "(c.start < :end AND c.end > :start)")
//    boolean existsOverlappingSchedule(@Param("startTime") LocalDateTime start,
//                                      @Param("endTime") LocalDateTime end);

    @Query("SELECT c FROM Criticism c WHERE c.start BETWEEN :startDate AND :endDate")
    List<Criticism> criticismBySemester(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
