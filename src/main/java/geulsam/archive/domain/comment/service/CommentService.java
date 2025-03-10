package geulsam.archive.domain.comment.service;

import geulsam.archive.domain.comment.dto.req.CommentUpdateReq;
import geulsam.archive.domain.comment.dto.req.CommentUploadReq;
import geulsam.archive.domain.comment.dto.res.CommentRes;
import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.comment.repository.CommentRepository;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PageRes<CommentRes> getCommentsByContentId(Pageable pageable, UUID contentId) {

        Content findContent = contentRepository.findById(contentId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        Page<Comment> commentPage = commentRepository.findByContentOrderByCreatedAtDesc(findContent, pageable);

        List<CommentRes> commentResList = commentPage.getContent().stream()
                .map(comment -> new CommentRes(comment, comment.getId()))
                .collect(Collectors.toList());

        return new PageRes<>(
                commentPage.getTotalPages(),
                commentResList
        );
    }

    /**
     * Level.SUSPENDED, Level.NORMAL 타입을 가진 User 만이 해당 기능에 접근 가능하다.
     * @param commentUploadReq Comment 객체 생성에 필요한 정보가 담긴 DTO
     * @param userId 로그인한 유저의 id
     * @return int 저장한 Comment 객체의 id
     */
    @Transactional
    public int upload(CommentUploadReq commentUploadReq, int userId) {

        Content findContent = contentRepository.findById(commentUploadReq.getContentId()).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        if(findUser.getLevel().equals(Level.ADMIN)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "댓글 등록 권한 없음");
        }

        Comment newComment = new Comment(
                commentUploadReq.getWriting(),
                LocalDateTime.now(),
                findUser,
                findContent
        );

        Comment savedComment = commentRepository.save(newComment);

        return savedComment.getId();
    }

    @Transactional
    public void delete(int commentId, int userId) {

        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Comment 없음"
        ));

        if (!findComment.getUser().getId().equals(userId) && !findUser.getLevel().equals(Level.ADMIN)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "사용자 권한 없음");
        }

        commentRepository.deleteById(findComment.getId());
    }

    @Transactional
    public void deleteAllCommentsByContentId(UUID contentId) {
        commentRepository.deleteAllByContentId(contentId);
    }

    @Transactional
    public CommentRes update(int commentId, CommentUpdateReq commentUpdateReq, int userId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Comment 없음"
        ));

        if (!findComment.getUser().getId().equals(userId)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "사용자 권한 없음");
        }

        findComment.changeWriting(commentUpdateReq.getWriting() + "(수정됨)");
        Comment savedComment = commentRepository.save(findComment);

        return new CommentRes(savedComment, savedComment.getId());
    }
}
