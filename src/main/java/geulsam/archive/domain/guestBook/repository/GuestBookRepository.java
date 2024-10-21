package geulsam.archive.domain.guestBook.repository;

import geulsam.archive.domain.guestBook.entity.GuestBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestBookRepository extends JpaRepository<GuestBook, Integer> {
    @Query("SELECT gb FROM GuestBook gb where gb.owner.id = :ownerId")
    Page<GuestBook> findGuestBooksByOwnerId(Pageable pageable, @Param("ownerId") int ownerId);

    @Modifying
    @Query("DELETE FROM GuestBook gb WHERE gb.owner.id =:userId OR gb.writer.id =:userId")
    int deleteByUser(@Param("userId") Integer userId);
}
