package geulsam.archive.domain.comment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PROTECTED)
public class CommentUploadReq {
    @Schema(description = "작품 아이디", example = "adsa-dsafdsa-vsadf-dsafdsa")
    private UUID contentId;
    @Schema(description = "댓글 내용", example = "너무 재밌게 잘 봤습니다! 전작이랑 분위기가 달라 신선했어요.")
    private String writing;
}
