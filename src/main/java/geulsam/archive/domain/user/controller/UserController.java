package geulsam.archive.domain.user.controller;

import geulsam.archive.domain.user.dto.req.LoginReq;
import geulsam.archive.domain.user.dto.req.SignupReq;
import geulsam.archive.domain.user.dto.res.CheckRes;
import geulsam.archive.domain.user.dto.res.LoginRes;
import geulsam.archive.domain.user.service.UserService;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 컨트롤러
     * @param signupReq 이름, 학번, 전화번호를 받아서 저장
     * @return
     */
    @PostMapping( "/signup")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "회원가입 성공",
                useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> signup(@RequestBody @Valid SignupReq signupReq){
        userService.signup(
                signupReq.getName(),
                signupReq.getSchoolNum(),
                signupReq.getPhone(),
                signupReq.getEmail(),
                signupReq.getJoinedAt(),
                signupReq.getIntroduce(),
                String.join(", ", signupReq.getKeyword()), // List<String>을 쉼표로 구분된 단일 String 으로 변경
                signupReq.getBirthDay()
        );

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("회원가입 성공").build()
        );
    }

    /**
     * 로그인 컨트롤러
     * @param loginReq 학번, 비밀번호를 변수로 가진 객체를 받아서 로그인 시도
     * @return 2개의 토큰, accessToken, RefreshToken 을 return
     * */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> login(@RequestBody @Valid LoginReq loginReq){
        LoginRes loginRes = userService.login(loginReq.getSchoolNum(), loginReq.getPassword());

        return ResponseEntity.ok().body(
                SuccessResponse.<LoginRes>builder()
                        .data(loginRes)
                        .status(HttpStatus.OK.value())
                        .message("로그인 성공")
                        .build()
        );
    }

    /**
     * 테스트용 컨트롤러
     * @return 로그인한 사용자의 아이디와 권한을 return
     */
    @GetMapping("/testing")
    @Operation(security = {
            @SecurityRequirement(name = "AccessToken"),
            @SecurityRequirement(name = "RefreshToken")
    })
    public ResponseEntity<String> testing(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok().body("hello! " + userDetails.getUserId() + " and you are " + authentication.getAuthorities());
    }

    /**
     * 유저의 아이디와 권한을 확인
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<SuccessResponse<CheckRes>> check(){
        /*유저 인증 객체를 가져옴*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*유저 권한 컬렉션을 스트링으로 변환*/
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        /*유저 인증 객체 생성*/
        CheckRes checkRes = new CheckRes(userDetails.getUserId(), roles);
        return ResponseEntity.ok().body(
                SuccessResponse.<CheckRes>builder()
                        .data(checkRes)
                        .status(HttpStatus.OK.value())
                        .message("유저 아이디와 권한 리턴")
                        .build()
        );
    }
}
