package geulsam.archive.domain.award.controller;

import geulsam.archive.domain.award.dto.res.AwardRes;
import geulsam.archive.domain.award.service.AwardService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/award")
public class AwardController {

    private final AwardService awardService;

    /**
     * 상 조회 API
     * DB에 있는 모든 상 리턴
     * @param page
     * @return PageRes<AwardRes>
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
                        .message("awards get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 상 조회 API
     * DB에 있는 모든 상 리턴
     * @param page
     * @return PageRes<AwardRes>
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
                        .message("awards get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }


}
