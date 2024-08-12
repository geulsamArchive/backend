package geulsam.archive.domain.criticismAuthor.controller;

import geulsam.archive.domain.criticismAuthor.dto.CriticismAuthorUploadReq;
import geulsam.archive.domain.criticismAuthor.entity.Condition;
import geulsam.archive.domain.criticismAuthor.service.CriticismAuthorService;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/criticismAuthor")
@Validated
public class CriticismAuthorController {

    private final CriticismAuthorService criticismAuthorService;

    @PostMapping()
    public ResponseEntity<SuccessResponse<Void>> upload(@RequestBody @Valid CriticismAuthorUploadReq criticismAuthorUploadReq){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        criticismAuthorService.upload(criticismAuthorUploadReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("합평 신청 업로드 성공").build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam int search, // 삭제하려는 신청이 있는 합평회 id
            @RequestParam int order // 삭제하려는 순서
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        // 신청받은 유저 아이디, 삭제하려는 신청이 있는 합평회 id 삭제하려는 순서
        criticismAuthorService.delete(userDetails.getUserId(), search, order, roles.get(0));

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("합평 신청 삭제 성공").build()
        );
    }

    @GetMapping("/toggle")
    public ResponseEntity<SuccessResponse<?>> toggleCondition(
            @RequestParam(defaultValue = "id") String field,
            @RequestParam int search
    ){
        Condition toggle = criticismAuthorService.toggle(search);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("해당 신청 " + toggle.toString() + " 로 전환 성공").build()
        );
    }
}
