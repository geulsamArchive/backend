package geulsam.archive.domain.criticismAuthor.repository;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismAuthor.entity.Condition;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CriticismAuthorRepository extends JpaRepository<CriticismAuthor, Integer> {
    @Query("SELECT ca FROM CriticismAuthor ca WHERE ca.criticism.start < :now")
    Page<CriticismAuthor> findCriticismAuthorBeforeNow(@Param("now") LocalDateTime now, Pageable pageable);

    /**
     * 합평 목록 중 userID와 일치하며
     * @param userID
     * @param genre
     * @return
     */
    @Query("SELECT ca FROM CriticismAuthor ca WHERE ca.condition = geulsam.archive.domain.criticismAuthor.entity.Condition.FIXED AND ca.author.id =: userID AND ca.genre =: genre AND ca.criticism.start >: now")
    List<CriticismAuthor> findAvailableDateTime(@Param("userID") Integer userID, @Param("genre") Genre genre, @Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM CriticismAuthor ca WHERE ca.criticism.id = :criticismId")
    int deleteByCriticismId(@Param("criticismId") Integer criticismId);

    @Modifying
    @Query("DELETE FROM CriticismAuthor ca WHERE ca.author.id = :userId")
    int deleteByUserId(@Param("userId") Integer userId);
}
