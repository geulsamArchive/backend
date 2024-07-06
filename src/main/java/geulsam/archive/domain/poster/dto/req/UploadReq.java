package geulsam.archive.domain.poster.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UploadReq {
    @Schema(description = "포스터 파일", example="example.jpg")
    private MultipartFile image;
    @Schema(description = "포스터 썸네일 파일 ", example="thumbnail.jpg")
    private MultipartFile thumbNail;
    @Schema(description = "포스터가 사용된 연도", example="2024")
    private int year;
    @Schema(description = "포스터 디자이너 이름", example="김철수")
    private String designer;
    @Schema(description = "포스터 판형", example="A4")
    private String plate;
}
