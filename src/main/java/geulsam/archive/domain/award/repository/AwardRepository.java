package geulsam.archive.domain.award.repository;

import geulsam.archive.domain.award.entitiy.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Integer> {
}
