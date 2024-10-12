package geulsam.archive.domain.refreshtoken.repository;

import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import geulsam.archive.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    /**
     * token String 을 사용하여 refresh token 객체 select
     * @param token
     * @return
     */
    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user = :user")
    List<RefreshToken> findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.token =:token")
    int deleteRefreshTokenByToken(String token);

    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :id")
    int deleteRefreshTokenByUserId(Integer id);
}
