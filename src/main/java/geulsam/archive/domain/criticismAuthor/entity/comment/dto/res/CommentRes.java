package geulsam.archive.domain.criticismAuthor.entity.comment.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import geulsam.archive.domain.criticismAuthor.entity.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentRes {
    /**Comment Response 객체 인덱스**/
    @Schema(example = "0")
    private int id;
    /**Comment 객체의 내용**/
    @Schema(example = "너무 재밌게 잘 봤습니다! 전작이랑 분위기가 달라 신선했어요.")
    private String writing;
    /**Comment 객체의 작성자 이름**/
    @Schema(example = "양희재")
    private String commenter;
    /**Comment 객체의 작성 일시**/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 M월 d일 HH:mm")
    @Schema(example = "2024년 3월 21일 11:10", type = "string")
    private LocalDateTime createdAt;

    public CommentRes(Comment comment, int id) {
        this.id = id;
        this.writing = comment.getWriting();
        this.commenter = comment.getUser().getName();
        this.createdAt = comment.getCreatedAt();
    }
}
