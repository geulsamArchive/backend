package geulsam.archive.domain.criticismAuthor.entity.comment.controller;

import geulsam.archive.domain.award.dto.req.CommentUpdateReq;
import geulsam.archive.domain.criticismAuthor.entity.comment.dto.req.CommentUploadReq;
import geulsam.archive.domain.criticismAuthor.entity.comment.dto.res.CommentRes;
import geulsam.archive.domain.criticismAuthor.entity.comment.service.CommentService;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 콘텐츠에 대한 댓글 조회
     * @param contentId 댓글을 조회할 콘텐츠의 고유 ID
     * @return List<CommentRes> 해당 content id에 대해 생성된 순서대로 정렬된 댓글 목록
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<CommentRes>>> getCommentsByContentId(
            @RequestParam(defaultValue = "id") UUID contentId
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

    /**
     * 댓글 등록
     * Level.SUSPENDED, Level.NORMAL 타입을 가진 User 만이 해당 기능에 접근 가능하다.
     * @param commentUploadReq Comment 객체 생성에 필요한 정보가 담긴 DTO
     * @return Integer 등록된 댓글의 ID
     */
    @PostMapping()
    public ResponseEntity<SuccessResponse<Integer>> upload(@RequestBody CommentUploadReq commentUploadReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        int commentId = commentService.upload(commentUploadReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Integer>builder()
                        .data(commentId)
                        .status(HttpStatus.CREATED.value())
                        .message("댓글 업로드 성공")
                        .build()
        );
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글의 고유 ID
     * @return null
     */
    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam int commentId
    ) {
        commentService.delete(commentId);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("댓글 삭제 성공")
                        .build()
        );
    }

    /**
     * 댓글 수정
     * 해당 comment id를 가진 댓글을 권한 확인과 함께 수정 후 CommentRes를 리턴
     * @param commentId 수정할 댓글의 고유 ID
     * @param commentUpdateReq 수정할 댓글의 정보가 담긴 DTO
     * @return CommentRes 수정된 댓글의 정보
     */
    @PutMapping()
    public ResponseEntity<SuccessResponse<CommentRes>> update(
            @RequestParam(defaultValue = "id") int commentId,
            @RequestBody CommentUpdateReq commentUpdateReq
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        CommentRes updatedCommentRes = commentService.update(commentId, commentUpdateReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<CommentRes>builder()
                        .data(updatedCommentRes)
                        .status(HttpStatus.OK.value())
                        .message("댓글 수정 성공")
                        .build()
        );
    }
}
