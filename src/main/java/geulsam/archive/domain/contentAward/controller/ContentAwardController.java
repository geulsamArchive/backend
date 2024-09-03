package geulsam.archive.domain.contentAward.controller;

import geulsam.archive.domain.contentAward.dto.req.PresentAwardReq;
import geulsam.archive.domain.contentAward.dto.res.ContentAwardRes;
import geulsam.archive.domain.contentAward.service.ContentAwardService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/content-award")
public class ContentAwardController {

    private final ContentAwardService contentAwardService;

    /**
     * 특정 기간의 수상 이력 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param startDate 조회 시작 날짜 (형식: yyyy-MM-dd)
     * @param endDate 조회 종료 날짜 (형식: yyyy-MM-dd)
     * @return PageRes<ContentAwardRes> 요청한 기간 내의 수상 이력을 포함하는 페이지 결과
     */
    @GetMapping("/period")
    public ResponseEntity<SuccessResponse<PageRes<ContentAwardRes>>> getAwardsByPeriod(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
            @RequestParam(defaultValue = "1") int page
            ) {

        startDate = (startDate != null) ? startDate : LocalDate.MIN;
        endDate = (endDate != null) ? endDate : LocalDate.MAX;

        Pageable pageable = PageRequest.of(page-1, 8, Sort.by("contentAwardAt").descending());

        PageRes<ContentAwardRes> contentAwardResList = contentAwardService.getAwardsByPeriod(startDate, endDate, pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<ContentAwardRes>>builder()
                        .data(contentAwardResList)
                        .message("awards during the period retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 작품 수상
     * @param presentAwardReq Award 수상에 필요한 정보가 담긴 DTO
     * @return null
     */
    @PostMapping()
    public ResponseEntity<SuccessResponse<String>> presentAward(@RequestBody @Valid PresentAwardReq presentAwardReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String awardName = contentAwardService.presentAward(presentAwardReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<String>builder()
                        .data(awardName)
                        .message("award presented successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
