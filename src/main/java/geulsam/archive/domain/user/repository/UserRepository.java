package geulsam.archive.domain.user.repository;

import geulsam.archive.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
