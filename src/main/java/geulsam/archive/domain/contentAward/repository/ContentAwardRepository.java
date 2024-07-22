package geulsam.archive.domain.contentAward.repository;

import geulsam.archive.domain.contentAward.entity.ContentAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContentAwardRepository extends JpaRepository<ContentAward, Integer> {
    List<ContentAward> findByContentId(UUID contentId);

    Page<ContentAward> findByContentAwardAtGreaterThanEqualAndContentAwardAtLessThanEqual(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
