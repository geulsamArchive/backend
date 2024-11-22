package geulsam.archive.domain.bookContent.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UploadReq {
    private Integer page;
    private String title;
    private String name;
    private String contentId;
}
