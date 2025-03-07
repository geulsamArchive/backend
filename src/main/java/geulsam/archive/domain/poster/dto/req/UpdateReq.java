package geulsam.archive.domain.poster.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class UpdateReq {
        @Schema(description = "포스터 파일", example="example.jpg")
        private Optional<MultipartFile> image = Optional.empty();

        @Schema(description = "포스터 썸네일 파일 ", example="thumbnail.jpg")
        private Optional<MultipartFile> thumbNail = Optional.empty();

        @Schema(description = "포스터가 사용된 연도", example="2024")
        private Integer year;

        @Schema(description = "포스터 디자이너 이름", example="김철수")
        private String designer;

        @Schema(description = "포스터 판형", example="A4")
        private String plate;
}
