package geulsam.archive.domain.criticismAuthor.entity.comment.repository;

import geulsam.archive.domain.criticismAuthor.entity.comment.entity.Comment;
import geulsam.archive.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByContentOrderByCreatedAtDesc(Content Content);
}
