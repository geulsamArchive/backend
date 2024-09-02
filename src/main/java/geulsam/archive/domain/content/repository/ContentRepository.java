package geulsam.archive.domain.content.repository;

import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import geulsam.archive.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {
    Page<Content> findAll(Pageable pageable);

    /**
     * Content 의 공개범위, 장르, 제목 포함 문자열 혹은 저자명 포함 문자열에 따라 적절한 Content를 찾는다.
     * @param isVisible Content 의 공개범위
     * @param genre Content 의 장르
     * @param keyword Content 의 제목 혹은 저자명을 검색하기 위한 키워드
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return 기준에 적합한 Content 들을 가진 페이지
     */
    @Query("SELECT c FROM Content c " +
            "WHERE c.isVisible = :isVisible " +
            "AND c.genre = :genre " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Content> findContentByFilters(
            @Param("isVisible") IsVisible isVisible,
            @Param("genre") Genre genre,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /**
     * Content 의 공개범위, 제목 포함 문자열 혹은 저자명 포함 문자열에 따라 적절한 Content를 찾는다.
     * @param isVisible Content 의 공개범위
     * @param keyword Content 의 제목 혹은 저자명을 검색하기 위한 키워드
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return 기준에 적합한 Content 들을 가진 페이지
     */
    @Query("SELECT c FROM Content c " +
            "WHERE c.isVisible = :visibleType " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Content> findByIsVisibleAndKeyword(
            @Param("visibleType") IsVisible isVisible,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Content> findByIsVisibleAndGenre(IsVisible isVisible, Genre genre, Pageable pageable);
    Page<Content> findByIsVisible(IsVisible isVisible, Pageable pageable);

    Page<Content> findTop8ByIsVisibleOrderByCreatedAtDesc(IsVisible isVisible, Pageable pageable);

    Page<Content> findByUserOrderByCreatedAtDesc(User findUser, Pageable pageable);
}
