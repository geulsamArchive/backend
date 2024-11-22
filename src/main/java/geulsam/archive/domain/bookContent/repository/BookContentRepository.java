package geulsam.archive.domain.bookContent.repository;

import geulsam.archive.domain.bookContent.entity.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookContentRepository extends JpaRepository<BookContent, UUID> {
    List<BookContent> findAllByBookId(UUID bookId);
}
