package geulsam.archive.global.security.jwt;

import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import geulsam.archive.global.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.Duration;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    Key key;

//    @Value("access-token-minutes")
//    private int accessTokenMinutes;
//
//    @Value("refresh-token-minutes")
//    private int refreshTokenMinutes;

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
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(10).toMillis()))
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

        User user = userRepository.findById(Integer.valueOf(id)).orElseThrow(() -> new RuntimeException("유저가 존재하지 않음"));

//        User user = userOptional.get();

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getPassword(), user.getLevel());

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    /**
     * Token 만료 시간을 Sec 로 리턴
     * @param token
     * @return
     */
    public int getTokenExpirationSec(String token){
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            long expirationTimeInMillis = claimsJws.getBody().getExpiration().getTime();
            return (int) (expirationTimeInMillis / 1000);
        } catch (ExpiredJwtException e){
            Date expiration = e.getClaims().getExpiration();
            if (expiration != null) {
                long expirationTimeInMillis = expiration.getTime();
                return (int) (expirationTimeInMillis / 1000);
            } else {
                throw new ArchiveException(ErrorCode.VALUE_ERROR, "로그인 오류. 관리자에게 문의해 주세요.");
            }
        }
    }

    /**
     * 현재 기준으로 토큰 만료 기간이 하루가 남았는지 확인하는 함수
     * @param token
     * @return
     */
    public boolean isDayExpiration(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Date expiration = claimsJws.getBody().getExpiration();
        Date now = new Date(); // 현재 시간

        // 하루(24시간)의 밀리초를 계산
        long oneDayInMillis = 24 * 60 * 60 * 1000;

        // 현재 시간으로부터 하루가 남았는지 확인
        return (expiration.getTime() - now.getTime()) <= oneDayInMillis;
    }
}
