package geulsam.archive.domain.comment.service;

import geulsam.archive.domain.award.dto.req.CommentUpdateReq;
import geulsam.archive.domain.comment.dto.req.CommentUploadReq;
import geulsam.archive.domain.comment.dto.res.CommentRes;
import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.comment.repository.CommentRepository;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
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
    public List<CommentRes> getCommentsByContentId(UUID contentId) {

        Content findContent = contentRepository.findById(contentId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        List<Comment> commentList = commentRepository.findByContentOrderByCreatedAtDesc(findContent);

        return commentList.stream()
                .map(comment -> new CommentRes(comment, commentList.indexOf(comment)))
                .collect(Collectors.toList());
    }

    @Transactional
    public int upload(CommentUploadReq commentUploadReq) {

        Content findContent = contentRepository.findById(commentUploadReq.getContentId()).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        User findUser = userRepository.findById(commentUploadReq.getUserId()).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));;

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
    public void delete(int id) {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 id의 comment 없음"
        ));

        commentRepository.deleteById(comment.getId());
    }

    @Transactional
    public CommentRes update(int commentId, CommentUpdateReq commentUpdateReq, int userId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Comment 없음"
        ));

        if (!findComment.getUser().getId().equals(userId)) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "사용자 권한 없음");
        }

        findComment.changeWriting(commentUpdateReq.getWriting() + "(수정됨)");
        Comment savedComment = commentRepository.save(findComment);

        return new CommentRes(savedComment, savedComment.getId());
    }
}
