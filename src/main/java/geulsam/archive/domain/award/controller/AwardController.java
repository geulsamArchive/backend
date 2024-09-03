package geulsam.archive.domain.award.controller;

import geulsam.archive.domain.award.dto.req.AwardUploadReq;
import geulsam.archive.domain.award.dto.res.AwardRes;
import geulsam.archive.domain.award.service.AwardService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/award")
public class AwardController {

    private final AwardService awardService;

    /**
     * DB에 있는 모든 상 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @return PageRes<AwardRes> 상 목록을 포함하는 페이지 결과
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<AwardRes>>> getAwards(
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("createdAt").descending());

        PageRes<AwardRes> awardResList = awardService.getAwards(pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<AwardRes>>builder()
                        .data(awardResList)
                        .message("awards retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 특정 연도에 대한 상 조회
     * @param year 조회할 연도
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @return PageRes<AwardRes> 특정 연도에 대한 상 목록을 포함하는 페이지 결과
     */
    @GetMapping("/{year}")
    public ResponseEntity<SuccessResponse<PageRes<AwardRes>>> getAwardsByYear(
            @PathVariable int year,
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("createdAt").descending());

        PageRes<AwardRes> awardResList = awardService.getAwardsByYear(year, pageable);

        System.out.print(awardResList);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<AwardRes>>builder()
                        .data(awardResList)
                        .message("awards retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 상 등록
     * Level.GRADUATED과 Level.SUSPENDED는 권한이 없다.
     * @param awardUploadReq Award 객체 생성에 필요한 정보가 담긴 DTO
     * @return Integer 저장한 상 객체의 고유 ID
     */
    @PostMapping()
    public ResponseEntity<SuccessResponse<Integer>> upload(@RequestBody AwardUploadReq awardUploadReq) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Integer awardId = awardService.upload(awardUploadReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Integer>builder()
                        .data(awardId)
                        .status(HttpStatus.OK.value())
                        .message("award added successfully")
                        .build()
        );
    }

    /**
     * 상 삭제
     * Level.GRADUATED과 Level.SUSPENDED는 권한이 없다.
     * @param awardId 삭제할 상의 고유 ID
     * @return null
     */
    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(@RequestParam int awardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        awardService.delete(awardId, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("award removed successfully")
                        .build()
        );
    }
}
