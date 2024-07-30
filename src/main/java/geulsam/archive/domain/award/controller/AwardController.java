package geulsam.archive.domain.award.controller;

import geulsam.archive.domain.award.dto.req.AwardUploadReq;
import geulsam.archive.domain.award.dto.res.AwardRes;
import geulsam.archive.domain.award.service.AwardService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * 상 업로드 API
     * @param awardUploadReq Award 객체 생성에 필요한 정보를 담은 DTO
     * @return Integer 저장한 Award 객체의 id
     */
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "상 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Integer>> upload(@RequestBody AwardUploadReq awardUploadReq) {

        Integer awardId = awardService.upload(awardUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Integer>builder()
                        .data(awardId)
                        .status(HttpStatus.CREATED.value())
                        .message("상 업로드 성공")
                        .build()
        );
    }

    /**
     * 관련 Content의 award 필드를 null로 설정하고 해당 Award를 삭제한다.
     * @param id 삭제할 상의 id 값
     * @return void
     */
    @DeleteMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상 삭제 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> delete(int id) {
        awardService.delete(id);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("상 삭제 성공")
                        .build()
        );
    }
}
