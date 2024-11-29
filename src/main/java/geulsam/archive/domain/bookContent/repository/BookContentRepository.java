package geulsam.archive.domain.bookContent.repository;

import geulsam.archive.domain.bookContent.entity.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookContentRepository extends JpaRepository<BookContent, UUID> {
    @Query("SELECT bc FROM BookContent bc WHERE bc.book.id =:bookId ORDER BY bc.page")
    List<BookContent> findAllByBookId(UUID bookId);
}
