package geulsam.archive.domain.user.repository;

import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT u FROM User u WHERE u.level =:level")
    Page<User> findByUserLevel(Level level, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.level =:level AND (u.name LIKE %:search% OR u.schoolNum LIKE %:search%)")
    Page<User> findByUserLevelAndSchoolNumOrName(Level level, Pageable pageable, String search);

    // user level 2개를 적용해서 찾을 수 있는 함수(1개는 admin 으로)
    @Query("SELECT u FROM User u WHERE (u.level = :level OR u.level = :admin) AND (u.name LIKE %:search% OR u.schoolNum LIKE %:search%)")
    Page<User> findByUserLevelAndAdminANDSchoolNumOrName(Level level, Level admin, Pageable pageable, String search);
}
