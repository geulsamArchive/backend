package geulsam.archive.domain.book.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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
//    private List<geulsam.archive.domain.bookContent.dto.req.UploadReq> bookContentList;
    @Schema(example = "[{page:26 title:제목 contentId:aa-bb-cc-dd name:김철수}]")
    private String bookContentList;
}
