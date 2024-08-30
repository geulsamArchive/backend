package geulsam.archive.domain.book.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UpdateReq {
    private Optional<MultipartFile> bookCover = Optional.empty();
    private Optional<MultipartFile> bookCoverThumbnail = Optional.empty();
    private Optional<MultipartFile> backCover = Optional.empty();
    private Optional<MultipartFile> backCoverThumbnail = Optional.empty();
    private Optional<MultipartFile> pdf = Optional.empty();
    private String designer;
    private String plate;
    private Integer pageNumber;
    private Integer year;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate release;
    private String title;
}
