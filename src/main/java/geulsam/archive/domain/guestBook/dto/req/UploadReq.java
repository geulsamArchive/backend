package geulsam.archive.domain.guestBook.dto.req;

import lombok.Data;

@Data
public class UploadReq {
    private Integer ownerId;
    private String writing;
}
