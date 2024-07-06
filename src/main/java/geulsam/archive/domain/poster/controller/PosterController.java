package geulsam.archive.domain.poster.controller;

import geulsam.archive.domain.poster.dto.req.UploadReq;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.service.PosterService;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/poster")
@Validated
public class PosterController {

    private final PosterService posterService;

    /**
     * DB 에 있는 poster 컬럼을 paging 해서 return
     * @return PageRes<PosterRes>
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<PosterRes>>> poster(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "asc") String order ){ //poster?page=1&order=asc

        // order String 에 따라 내림차순/오름차순 선정
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //pageNumber 는 클라이언트에서 1로 넘어오지만 Spring 의 페이징 기능은 페이지가 0부터 시작
        // pageSize 는 12로 고정, 정렬 기준 속성도 year 로 고정
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by(direction, "year"));
//        List<PosterRes> poster = posterService.poster(pageable);

        PageRes<PosterRes> poster = posterService.poster(pageable);
        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<PosterRes>>builder()
                        .data(poster)
                        .message("posters get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * Poster upload 메소드
     * @param uploadReq Poster 객체 생성에 필요한 정보를 담은 메서드
     * @return null
     */
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "포스터 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> upload(@ModelAttribute UploadReq uploadReq){

        posterService.upload(uploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("포스터 업로드 성공")
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam(defaultValue = "id") String field,
            @RequestParam String search
    ){
        posterService.delete(field, search);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("포스터 삭제 성공")
                        .build()
        );
    }
}
