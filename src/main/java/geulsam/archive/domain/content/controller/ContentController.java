package geulsam.archive.domain.content.controller;

import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.service.ContentService;
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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/content")
public class ContentController {

    private final ContentService contentService;

    /**
     * 작품 조회 API
     * DB에 있는 모든 작품 리턴
     * @return PageRes<ContentRes>
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<ContentRes>>> getContents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("createdAt").descending());

        PageRes<ContentRes> contentResList = contentService.getContents(field, search, pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<ContentRes>>builder()
                        .data(contentResList)
                        .message("contents get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 작품 세부정보 조회 API
     * 해당 id를 가진 content의 세부 정보 리턴
     * @return ContentInfoRes
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ContentInfoRes>> getContentInfo(@PathVariable String id) {

        ContentInfoRes contentInfoRes = contentService.getContentInfo(id);

        return ResponseEntity.ok().body(
                SuccessResponse.<ContentInfoRes>builder()
                        .data(contentInfoRes)
                        .message("contents get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 작품 업로드 API
     * @param contentUploadReq Content 객체 생성에 필요한 정보를 담은 DTO
     * @return UUID 저장한 Content 객체의 id
     */
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "작품 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<UUID>> upload(@ModelAttribute ContentUploadReq contentUploadReq) {

        UUID contentId = contentService.upload(contentUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<UUID>builder()
                        .data(contentId)
                        .status(HttpStatus.CREATED.value())
                        .message("작품 업로드 성공")
                        .build()
        );
    }

    /**
     * 작품 삭제 API
     * @param field 기본값은 id, 삭제하고 싶은 작품이 가진 필드를 지정(ex. id, user, book...)
     * @param search 기본값 없음. 삭제할 작품의 필드 값을 지정
     * @return null
     */
    @DeleteMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "작품 삭제 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam(defaultValue = "id") String field,
            @RequestParam String search
    ) {
        contentService.delete(field, search);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("작품 삭제 성공")
                        .build()
        );
    }
}
