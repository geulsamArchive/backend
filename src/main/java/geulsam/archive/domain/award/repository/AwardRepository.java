package geulsam.archive.domain.award.repository;

import geulsam.archive.domain.award.entitiy.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AwardRepository extends JpaRepository<Award, Integer> {
    Page<Award> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
