package geulsam.archive.domain.content.repository;

import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import geulsam.archive.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            IsVisible isVisible,
            Genre genre,
            String keyword,
            Pageable pageable
    );
    @Query("SELECT c FROM Content c " +
            "WHERE (c.isVisible = :isVisible1 or c.isVisible = :isVisible2) " +
            "AND c.genre = :genre " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Content> findContentByFilters(
            IsVisible isVisible1,
            IsVisible isVisible2,
            Genre genre,
            String keyword,
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
            "WHERE c.isVisible = :isVisible " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Content> findByIsVisibleAndKeyword(
            IsVisible isVisible,
            String keyword,
            Pageable pageable
    );
    @Query("SELECT c FROM Content c " +
            "WHERE (c.isVisible = :isVisible1 or c.isVisible = :isVisible2) " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Content> findByIsVisibleAndKeyword(
            IsVisible isVisible1,
            IsVisible isVisible2,
            String keyword,
            Pageable pageable
    );


    Page<Content> findByIsVisibleAndGenre(IsVisible isVisible, Genre genre, Pageable pageable);
    @Query("SELECT c FROM Content c WHERE c.genre = :genre AND (c.isVisible = :isVisible1 OR c.isVisible = :isVisible2) ORDER BY c.createdAt DESC")
    Page<Content> findByIsVisibleAndGenre(IsVisible isVisible1, IsVisible isVisible2, Genre genre, Pageable pageable);


    Page<Content> findByIsVisible(IsVisible isVisible, Pageable pageable);
    @Query("SELECT c FROM Content c WHERE c.isVisible = :isVisible1 OR c.isVisible = :isVisible2 ORDER BY c.createdAt DESC")
    Page<Content> findByIsVisible(IsVisible isVisible1, IsVisible isVisible2, Pageable pageable);


    Page<Content> findByIsVisibleOrderByCreatedAtDesc(IsVisible isVisible, Pageable pageable);
    @Query("SELECT c FROM Content c WHERE c.isVisible = :isVisible1 OR c.isVisible = :isVisible2 ORDER BY c.createdAt DESC")  // LIMIT 8
    Page<Content> findByIsVisibleOrderByCreatedAtDesc(IsVisible isVisible1, IsVisible isVisible2, Pageable pageable);


    Page<Content> findByUserOrderByCreatedAtDesc(User findUser, Pageable pageable);


    Page<Content> findByUserAndIsVisible(User findUser, IsVisible isVisible, Pageable pageable);
    @Query("SELECT c FROM Content c WHERE c.user = :user AND (c.isVisible = :isVisible1 OR c.isVisible = :isVisible2) ORDER BY c.createdAt DESC")
    Page<Content> findByUserAndIsVisible(IsVisible isVisible1, IsVisible isVisible2, User user, Pageable pageable);

    void deleteAllByUserId(int userId);
}
