package geulsam.archive.global.security.jwt;

import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.exception.RestException;
import geulsam.archive.global.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    Key key;

    @PostConstruct
    public void init(){
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * AccessJWT 생성
     * 유효 시간은 10분
     * @param(Integer) userID
     * @return
     */
    public String createAccessToken(Integer userID){
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes((10)).toMillis()))
                .setSubject(userID.toString())
                .signWith(key)
                .compact();
    }

    /**
     * RefreshToken 생성
     * 유효 시간은 7일
     * @return
     */
    public String createRefreshToken(){
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(7).toMillis()))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰 유효성 확인
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰을 받아서 토큰의 subject 를 String 으로 추출
     * @param(String) token
     * @return
     */
    public String getID(String token){
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return claimsJws.getBody().getSubject();
    }

    /**
     * 토큰을 받아서 인증 객체를 생성
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token){
        String id = this.getID(token);

//        Optional<User> userOptional = userRepository.findById(Integer.valueOf(id));

        User user = userRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new RestException("유저가 존재하지 않음"));

//        User user = userOptional.get();

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getPassword(), user.getLevel());

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
