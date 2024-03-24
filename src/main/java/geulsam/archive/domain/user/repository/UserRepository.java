package geulsam.archive.domain.user.repository;

import geulsam.archive.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPARepository를 사용한 UserRepository
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 학번으로 User 객체 select
     * @param schoolNum (String)
     * @return
     */
    Optional<User> findBySchoolNum(String schoolNum);
}
