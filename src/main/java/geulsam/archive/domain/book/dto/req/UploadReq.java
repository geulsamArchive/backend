package geulsam.archive.domain.book.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UploadReq {
    private MultipartFile bookCover;
    private MultipartFile bookCoverThumbnail;
    private MultipartFile backCover;
    private MultipartFile backCoverThumbnail;
    private MultipartFile pdf;
    private String designer;
    private String plate;
    private int pageNumber;
    private int year;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate release;
    private String title;

}
