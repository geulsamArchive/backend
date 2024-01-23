package geulsam.archive.domain.contentAward.repository;

import geulsam.archive.domain.contentAward.entity.ContentAward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentAwardRepository extends JpaRepository<ContentAward, Integer> {
}
