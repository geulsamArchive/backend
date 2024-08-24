package geulsam.archive.domain.criticismAuthor.repository;

import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CriticismAuthorRepository extends JpaRepository<CriticismAuthor, Integer> {
    @Query("SELECT ca FROM CriticismAuthor ca WHERE ca.criticism.start < :now")
    Page<CriticismAuthor> findCriticismAuthorBeforeNow(@Param("now") LocalDateTime now, Pageable pageable);
    @Query("SELECT ca FROM CriticismAuthor ca WHERE ca.content.id = :contentId")
    Optional<CriticismAuthor> findByContentId(@Param("contentId") UUID contentId);
}
