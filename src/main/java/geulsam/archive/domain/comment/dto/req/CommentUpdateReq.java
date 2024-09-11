package geulsam.archive.domain.comment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUpdateReq {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Schema(example = "너무 재밌게 잘 봤습니다! 전작이랑 분위기가 달라 신선했어요.")
    private String writing;
}
