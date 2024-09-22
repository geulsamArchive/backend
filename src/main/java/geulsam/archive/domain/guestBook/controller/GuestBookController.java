package geulsam.archive.domain.guestBook.controller;

import geulsam.archive.domain.criticismLog.dto.res.CriticismLogRes;
import geulsam.archive.domain.guestBook.dto.req.UploadReq;
import geulsam.archive.domain.guestBook.dto.res.GuestBookRes;
import geulsam.archive.domain.guestBook.service.GuestBookService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/guestBook")
public class GuestBookController {

    private final GuestBookService guestBookService;

    /**
     * ownerId로 GuestBook 페이지 처리해서
     * @param page
     * @param ownerId
     * @return
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<GuestBookRes>>> criticismLog(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = true) int ownerId
    ){
        Pageable pageable = PageRequest.of(page-1, 5, Sort.by("createdAt").descending());

        PageRes<GuestBookRes> guestBookResPageRes = guestBookService.guestBook(pageable, ownerId);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<GuestBookRes>>builder()
                        .data(guestBookResPageRes)
                        .message("CriticismLog get Success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "방명록 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> upload(@RequestBody UploadReq uploadReq){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        guestBookService.upload(uploadReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("방명록 업로드 성공")
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam Integer id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        /*유저 권한 컬렉션을 스트링으로 변환*/
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        guestBookService.delete(id, roles.get(0), userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("방명록 삭제 성공")
                        .build()
        );
    }
}
