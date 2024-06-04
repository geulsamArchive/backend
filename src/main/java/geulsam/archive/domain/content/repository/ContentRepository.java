package geulsam.archive.domain.content.repository;

import geulsam.archive.domain.content.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Integer> {

    Page<Content> findAll(Pageable pageable);
}
