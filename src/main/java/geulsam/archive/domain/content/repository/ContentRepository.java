package geulsam.archive.domain.content.repository;

import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContentRepository extends JpaRepository<Content, UUID> {
    Page<Content> findByGenreAndNameContainingOrUser_NameContaining(Genre genre, String nameKeyword, String userNameKeyword, Pageable pageable);
    Page<Content> findByGenre(Genre genre, Pageable pageable);
    Page<Content> findByNameContainingOrUser_NameContaining(String nameKeyword, String userNameKeyword, Pageable pageable);
    Page<Content> findAll(Pageable pageable);
    Page<Content> findTop8ByOrderByCreatedAtDesc(Pageable pageable);
}
