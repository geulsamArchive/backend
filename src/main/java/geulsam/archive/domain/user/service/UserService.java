package geulsam.archive.domain.user.service;

import geulsam.archive.domain.refreshtoken.entity.RefreshToken;
import geulsam.archive.domain.refreshtoken.repository.RefreshTokenRepository;
import geulsam.archive.domain.user.dto.req.PasswordReq;
import geulsam.archive.domain.user.dto.req.UpdateReq;
import geulsam.archive.domain.user.dto.res.AuthorRes;
import geulsam.archive.domain.user.dto.res.LoginRes;
import geulsam.archive.domain.user.dto.res.UserOneRes;
import geulsam.archive.domain.user.dto.res.UserRes;
import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import geulsam.archive.global.security.jwt.JwtProvider;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender javaMailSender;

    /**
     * 유저 저장 트랜잭션
     * @param name(이름) : String
     * @param schoolNum(학번) : String
     * @param phone(전화번호) : String
     */
    @Transactional
    public void signup(String name, String schoolNum, String phone, String email, Year joinedAt, String introduce, String keyword, LocalDate birthDay) {

        /*존재하는 사용자이면 exception return*/
        if(userRepository.findBySchoolNum(schoolNum).isPresent()){
//            throw new RuntimeException("이미 존재하는 사용자입니다.");
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "이미 존재하는 사용자입니다.");
        }

        //임시비밀번호 생성
        String tempPassword = RandomStringUtils.randomAlphanumeric(10) + RandomStringUtils.random(2, true, true);

        // 임시비밀번호로 메일 발송
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Guelsam에서 임시 비밀번호 발급 안내");

            String htmlMsg = "<h3>안녕하세요, " + name + " 회원님</h3>"
                    + "<p>임시 비밀번호를 발급해 드립니다.</p>"
                    + "<div style='border: 2px solid #000; padding: 10px; width: fit-content; background-color: #f3f3f3;'>"
                    + "<strong style='font-size: 1.2em;'>" + tempPassword + "</strong>"
                    + "</div>"
                    + "<p>로그인 후에 비밀번호를 바꿔 주세요.</p>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "이메일을 발송하는 데 실패했습니다. 메일 주소를 확인해 주세요.");
        }

        /*user 생성자로 신규 유저 생성
        * password 는 학번을 사용하되, 암호화하여 저장한다.*/
        User user = new User(
                name,
                passwordEncoder.encode(tempPassword),
                schoolNum,
                Level.NORMAL,
                LocalDateTime.now(),
                email,
                phone,
                joinedAt,
                introduce,
                keyword,
                birthDay
        );
        /*유저 저장*/
        userRepository.save(user);
    }


    /**
     * User Login 트랜잭션
     * @param schoolNum: 유저 학번(String)
     * @param password: 유저 비밀번호(String)
     * @return: AccessToken, RefreshToken 이 있는 LoginRes 객체
     */
    @Transactional
    public LoginRes login(String schoolNum, String password){

        /* 학번으로 유저 탐색*/
        User user = userRepository.findBySchoolNum(schoolNum).orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR,"존재하지 않는 사용자"));

        /* 유저 객체가 존재한다면 비밀번호가 맞는지 확인*/
        if(passwordEncoder.matches(password, user.getPassword())){
            /* 비밀번호가 맞으면 유저 아이디로 accessToken 발급, refreshToken 발급*/
            String accessToken = jwtProvider.createAccessToken(user.getId());
            String refreshToken = jwtProvider.createRefreshToken();

            /*refreshTokenRepository 에서 같은 아이디를 가진 유저 탐색*/
            Optional<RefreshToken> userOptional = refreshTokenRepository.findByUser(user);

            /*이미 해당 유저에 대한 refreshToken 이 존재한다면*/
            if(userOptional.isPresent()){
                /*토큰 업데이트*/
                userOptional.get().changeTokenValue(refreshToken);
            } else {
                /* 존재하지 않는다면 refreshToken 은 tokenRepository 에 저장*/
                RefreshToken refreshTokenObject = new RefreshToken(refreshToken, user);
                refreshTokenRepository.save(refreshTokenObject);
            }

            /* 토큰 2개를 담은 객체 리턴 */
            return new LoginRes(accessToken, refreshToken);
        } else {
            /* 비밀번호가 맞지 않으면 비밀번호 불일치 예외 생성 후 전달*/
            throw new ArchiveException(ErrorCode.VALUE_ERROR,"비밀번호 불일치");
        }
    }

    @Transactional
    public UserOneRes findOneById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 사용자 없음"));
        return(new UserOneRes(user));
    }

    @Transactional
    public void update(int id, UpdateReq updateReq) {
        User user = userRepository.findById(id).orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 사용자 없음"));
        user.updateByPutReq(updateReq);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void checkSchoolNum(String search) {
        Optional<User> bySchoolNum = userRepository.findBySchoolNum(search);
        if(bySchoolNum.isPresent()){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "해당 학번 이미 가입됨. 관리자에게 문의하세요");
        }
    }

    @Transactional
    public void delete(Integer userId, String role, String schoolNum) {
        if (!userRepository.existsById(userId)) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다.");
        }

        userRepository.deleteById(userId);
    }

    @Transactional
    public void updatePassword(PasswordReq passwordReq, int search) {
        User user = userRepository.findById(search).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다.")
        );

        if(passwordEncoder.matches(passwordReq.getOldPassword(), user.getPassword())){
            user.updatePassword(passwordEncoder.encode(passwordReq.getNewPassword()));
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR,"입력하신 비밀번호가 올바르지 않습니다.");
        }
    }

    @Transactional
    public void updatePasswordAdmin(PasswordReq passwordReq, int search) {
        User user = userRepository.findById(search).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다.")
        );
        user.updatePassword(passwordEncoder.encode(passwordReq.getNewPassword()));
    }

    @Transactional(readOnly = true)
    public void checkPassword(String password, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "입력하신 비밀번호가 다릅니다.");
        }
    }

    /**
     * 유저 레벨 업데이트
     * @param level 업데이트할 레벨
     * @param userId 유저 아이디
     */
    @Transactional
    public void level(Level level, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다."));
        user.updateLevel(level);
    }

    @Transactional(readOnly = true)
    public PageRes<UserRes> user(Pageable pageable){
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserRes> userResList = userPage.getContent().stream()
                .map(user -> new UserRes(user, userPage.getContent().indexOf(user)))
                .collect(Collectors.toList());

        return new PageRes<>(
                userPage.getTotalPages(),
                userResList
        );
    }

    @Transactional
    public void resetPassword(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다."));

        String tempPassword = RandomStringUtils.randomAlphanumeric(12);

        // 임시비밀번호로 메일 발송
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Guelsam에서 임시 비밀번호 발급 안내");

            String htmlMsg = "<h3>안녕하세요, " + user.getName() + " 회원님</h3>"
                    + "<p>임시 비밀번호를 발급해 드립니다.</p>"
                    + "<div style='border: 2px solid #000; padding: 10px; width: fit-content; background-color: #f3f3f3;'>"
                    + "<strong style='font-size: 1.2em;'>" + tempPassword + "</strong>"
                    + "</div>"
                    + "<p>로그인 후에 비밀번호를 바꿔 주세요.</p>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "이메일을 발송하는 데 실패했습니다. 메일 주소를 확인해 주세요.");
        }

        user.updatePassword(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public AuthorRes author(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "사용자가 존재하지 않습니다."));

        return new AuthorRes(user);
    }
}
