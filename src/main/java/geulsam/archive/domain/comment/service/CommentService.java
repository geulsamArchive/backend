package geulsam.archive.domain.comment.service;

import geulsam.archive.domain.comment.dto.res.CommentRes;
import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.comment.repository.CommentRepository;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;

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
}
