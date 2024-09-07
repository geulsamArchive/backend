package geulsam.archive.domain.user.controller;

import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.user.dto.req.*;
import geulsam.archive.domain.user.dto.res.*;
import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.service.UserService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
                .map(GrantedAuthority::getAuthority)
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

    @GetMapping("/one")
    public ResponseEntity<SuccessResponse<UserOneRes>> one(
            @RequestParam(defaultValue = "0") int search
    ){
        /*유저 인증 객체를 가져옴*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*유저 권한 컬렉션을 스트링으로 변환*/
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        // 역할이 ADMIN 이면 필드 사용
        if(roles.get(0).contains(Level.ADMIN.toString())){
            UserOneRes userById = userService.findOneById(search);

            return ResponseEntity.ok().body(
                    SuccessResponse.<UserOneRes>builder()
                            .data(userById)
                            .status(HttpStatus.OK.value())
                            .message(Level.ADMIN + " 권한 USER " + search + " 정보")
                            .build()
            );
        } else {
            // NORMAL 이면 user 인증 객체 사용
            UserOneRes userById = userService.findOneById(userDetails.getUserId());

            return ResponseEntity.ok().body(
                    SuccessResponse.<UserOneRes>builder()
                            .data(userById)
                            .status(HttpStatus.OK.value())
                            .message(Level.NORMAL + " 권한 USER " + userDetails.getUserId() + " 정보")
                            .build()
            );
        }
    }

    @PutMapping()
    public ResponseEntity<SuccessResponse<Void>> put(
            @RequestBody UpdateReq updateReq,
            @RequestParam(defaultValue = "0") int search)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*유저 권한 컬렉션을 스트링으로 변환*/
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        // 역할이 ADMIN 이면 필드 사용
        if(roles.get(0).contains(Level.ADMIN.toString())){
            userService.update(search, updateReq);
        } else {
            // NORMAL 이면 user 인증 객체 사용
            userService.update(userDetails.getUserId(), updateReq);
        }

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("유저 정보 업데이트")
                        .build()
        );
    }

    /**
     * 학번 중복 체크 - 정보 노출의 위험이 있어 POST 로 작성
     * @return
     */
    @PostMapping("/checkSchoolNum")
    public ResponseEntity<SuccessResponse<Void>> checkSchoolNum(
            @RequestBody @Valid CheckSchoolNumReq checkSchoolNumReq
    ){
        userService.checkSchoolNum(checkSchoolNumReq.getSchoolNum());
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("입력하신 학번은 처음으로 가입하는 사용자입니다")
                        .build()
        );
    }

    /**
     *
     * @param checkPasswordReq
     * @return
     */
    @PostMapping("/checkPassword")
    public ResponseEntity<SuccessResponse<Void>> checkPassword(
            @RequestBody @Valid CheckPasswordReq checkPasswordReq
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        userService.checkPassword(checkPasswordReq.getPassword(), userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("입력하신 비밀번호가 일치합니다.")
                        .build()
        );
    }

    /**
     * 유저 개체 삭제
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam String schoolNum
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        userService.delete(userDetails.getUserId(), roles.get(0), schoolNum);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("회원 탈퇴 완료")
                        .build()
        );
    }

    /**
     * 
     * @param search
     * @param passwordReq
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<SuccessResponse<Void>> putPassword(
            @RequestParam(defaultValue = "0") int search,
            @RequestBody PasswordReq passwordReq
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*유저 권한 컬렉션을 스트링으로 변환*/
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        // 역할이 ADMIN 이면 필드 사용
        if(roles.get(0).contains(Level.ADMIN.toString())){
            userService.updatePasswordAdmin(passwordReq, search);
        } else {
            // NORMAL 이면 user 인증 객체 사용
            userService.updatePassword(passwordReq, userDetails.getUserId());
        }

        return ResponseEntity.ok().body
                (
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("비밀번호 변경 완료. 재로그인 필요")
                        .build()
        );
    }

    /**
     * 유저 레벨 변경
     * @return
     */
    @PutMapping("/level")
    public ResponseEntity<SuccessResponse<Void>> level(
            @RequestBody LevelUpdateReq levelUpdateReq
    ){
        userService.level(levelUpdateReq.getLevel(), levelUpdateReq.getUserId());

        return ResponseEntity.ok().body
                (
                        SuccessResponse.<Void>builder()
                                .data(null)
                                .status(HttpStatus.OK.value())
                                .message("유저 등급 변경 완료")
                                .build()
                );
    }

    /**
     * 유저 전체 정보
     * @param page 요청할 페이지 넘버
     * @param order 유저 정렬 순서 asc or desc
     * @return
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<UserRes>>> user(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "asc") String order
    ){
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //pageNumber 는 클라이언트에서 1로 넘어오지만 Spring 의 페이징 기능은 페이지가 0부터 시작
        // pageSize 는 12로 고정, 정렬 기준 속성도 year 로 고정
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by(direction, "name"));

        PageRes<UserRes> user = userService.user(pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<UserRes>>builder()
                        .data(user)
                        .message("user get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 유저 비밀번호 리셋
     * @param id 리셋할 유저 아이디
     * @return
     */
    @GetMapping("/resetPassword")
    public ResponseEntity<SuccessResponse<Void>> resetPassword(
            @RequestParam(defaultValue = "0") int id
    ){
        userService.resetPassword(id);

        return ResponseEntity.ok().body
                (
                        SuccessResponse.<Void>builder()
                                .data(null)
                                .status(HttpStatus.OK.value())
                                .message("유저 비밀번호 리셋 완료")
                                .build()
                );
    }

    /**
     * 유저(작가)의 개인정보 리턴
     * @param id
     * @return
     */
    @GetMapping("/author")
    public ResponseEntity<SuccessResponse<AuthorRes>> author(
            @RequestParam(defaultValue = "0") int id
    ){
        AuthorRes authorRes = userService.author(id);

        return ResponseEntity.ok().body
                (
                        SuccessResponse.<AuthorRes>builder()
                                .data(authorRes)
                                .status(HttpStatus.OK.value())
                                .message("작가 개인 소개")
                                .build()
                );
    }
}