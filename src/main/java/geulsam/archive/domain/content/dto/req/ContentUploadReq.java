package geulsam.archive.domain.content.dto.req;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ContentUploadReq {
    @Schema(description = "문집 아이디", example = "adsa-dsafdsa-vsadf-dsafdsa")
    private UUID bookId;
    @Schema(description = "작품 제목", example = "때때로 나는 회색분자라는 소리를 듣는다")
    @NotNull(message = "작품 제목은 Null 일 수 없습니다")
    private String name;
    @Schema(description = "pdf 형식의 작품 파일")
    private MultipartFile pdf;
    @Schema(description = "html 형식의 작품 파일")
    private MultipartFile html;
    @Schema(description = "작품 장르", example = "NOVEL")
    @NotNull(message = "장르는 Null 일 수 없습니다")
    private Genre genre;
    @Schema(description = "작품 공개여부", example = "EVERY")
    @NotNull(message = "작품 공개여부는 Null 일 수 없습니다")
    private IsVisible isVisible;
    @Schema(description = "문집에 속한 페이지", example = "1")
    private Integer bookPage;
    @Schema(description = "작품 소개", example = "황금시대에나 통하는 수법이지. 비싸고 비이성적일수록 근사하다고 믿는 시대에. 그 아이들이 커서 자기 고향을 어디라고 말할까?")
    @NotNull(message = "작품 소개는 Null 일 수 없습니다")
    @NotEmpty(message = "작품 소개는 빈 값 일 수 없습니다")
    private String sentence;
}
