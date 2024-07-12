package geulsam.archive.domain.content.repository;

import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    Page<Content> findByGenreAndNameContaining(Genre genre, String name, Pageable pageable);
    Page<Content> findByGenre(Genre genre, Pageable pageable);
    Page<Content> findByNameContaining(String name, Pageable pageable);
    Page<Content> findAll(Pageable pageable);
}
