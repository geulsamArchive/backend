package geulsam.archive.domain.contentAward.repository;

import geulsam.archive.domain.contentAward.entity.ContentAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContentAwardRepository extends JpaRepository<ContentAward, Integer> {
    List<ContentAward> findByContentId(UUID contentId);
}
