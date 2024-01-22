package geulsam.archive.domain.criticism.repository;

import geulsam.archive.domain.criticism.entity.Criticism;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriticismRepository extends JpaRepository<Criticism, Integer> {
}
