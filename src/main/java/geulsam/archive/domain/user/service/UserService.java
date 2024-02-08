package geulsam.archive.domain.user.service;

import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import geulsam.archive.domain.refreshtoken.repository.RefreshTokenRepository;
import geulsam.archive.domain.user.dto.res.LoginRes;
import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.exception.RestException;
import geulsam.archive.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 유저 저장 트랜잭션
     * @param name(이름) : String
     * @param schoolNum(학번) : String
     * @param phone(전화번호) : String
     */
    @Transactional
    public void signup(String name, String schoolNum, String phone){

        /*존재하는 사용자이면 exception return*/
        if(userRepository.findBySchoolNum(schoolNum).isPresent()){
            throw new RestException("이미 존재하는 사용자입니다.");
        }

        /*user 생성자로 신규 유저 생성
        * password 는 학번을 사용하되, 암호화하여 저장한다.*/
        User user = new User(
                name,
                passwordEncoder.encode(schoolNum),
                schoolNum,
                Level.NORMAL,
                LocalDateTime.now()
        );

        /*유저 전화번호 변경*/
        user.changePhone(phone);

        /*유저 저장*/
        userRepository.save(user);
    }

    @Transactional
    public LoginRes login(String schoolNum, String password){

        /* 학번으로 유저 탐색*/
        User user = userRepository.findBySchoolNum(schoolNum).orElseThrow(() -> new RestException("존재하지 않는 사용자"));

        /* 유저 객체가 존재한다면 비밀번호가 맞는지 확인*/
        if(passwordEncoder.matches(password, user.getPassword())){
            /* 비밀번호가 맞으면 유저 아이디로 accessToken 발급, refreshToken 발급*/
            String accessToken = jwtProvider.createAccessToken(user.getId());
            String refreshToken = jwtProvider.createRefreshToken();

            /* refreshToken 은 tokenRepository 에 저장*/
            RefreshToken refreshTokenObject = new RefreshToken(user.getId(), refreshToken);
            refreshTokenRepository.save(refreshTokenObject);

            /* 토큰 2개를 담은 객체 리턴 */
            return new LoginRes(accessToken, refreshToken);
        } else {
            /* 비밀번호가 맞지 않으면 비밀번호 불일치 예외 생성 후 전달*/
            throw new RestException("비밀번호 불일치");
        }
    }

}
