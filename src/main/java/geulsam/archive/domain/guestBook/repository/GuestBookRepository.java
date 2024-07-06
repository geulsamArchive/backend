package geulsam.archive.domain.guestBook.repository;

import geulsam.archive.domain.guestBook.entity.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GuestBookRepository extends JpaRepository<GuestBook, Integer> {
}
