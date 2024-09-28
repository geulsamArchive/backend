package geulsam.archive.domain.comment.repository;

import geulsam.archive.domain.comment.entity.Comment;
import geulsam.archive.domain.content.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByContentOrderByCreatedAtDesc(Content Content, Pageable pageable);
}
