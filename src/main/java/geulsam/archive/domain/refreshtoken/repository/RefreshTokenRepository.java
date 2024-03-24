package geulsam.archive.domain.refreshtoken.repository;

import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    /**
     * token String 을 사용하여 refresh token 객체 select
     * @param token
     * @return
     */
    Optional<RefreshToken> findByToken(String token);
}
