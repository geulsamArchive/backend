package geulsam.archive.domain.guestBook.dto.res;

import geulsam.archive.domain.guestBook.entity.GuestBook;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestBookRes {
    private int id;
    private int guestBookId;
    private String writerName;
    private LocalDate createdAt;
    private String writing;

    public GuestBookRes(GuestBook guestBook, int id){
        this.id = id;
        this.guestBookId = guestBook.getId();
        this.writerName = guestBook.getWriter().getName();
        this.createdAt = guestBook.getCreatedAt();
        this.writing = guestBook.getWriting();
    }
}
