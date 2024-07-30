package geulsam.archive.domain.comment.controller;

import geulsam.archive.domain.comment.dto.req.CommentUploadReq;
import geulsam.archive.domain.comment.dto.res.CommentRes;
import geulsam.archive.domain.comment.service.CommentService;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 조회 API
     * 해당 content id를 가진 모든 comment를 생성이 오래된 순서로 리턴
     * @param contentId
     * @return List<CommentRes>
     */
    @GetMapping("/{contentId}")
    public ResponseEntity<SuccessResponse<List<CommentRes>>> getCommentsByContentId(
            @PathVariable UUID contentId
    ) {
        List<CommentRes> commentResList = commentService.getCommentsByContentId(contentId);

        return ResponseEntity.ok().body(
                SuccessResponse.<List<CommentRes>>builder()
                        .data(commentResList)
                        .message("comments by content id get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "댓글 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Integer>> upload(@RequestBody CommentUploadReq commentUploadReq) {
        int commentId = commentService.upload(commentUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Integer>builder()
                        .data(commentId)
                        .status(HttpStatus.CREATED.value())
                        .message("댓글 업로드 성공")
                        .build()
        );
    }

    @DeleteMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 삭제 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam int id
    ) {
        commentService.delete(id);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("댓글 삭제 성공")
                        .build()
        );
    }
}
