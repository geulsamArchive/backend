package geulsam.archive.domain.content.controller;

import geulsam.archive.domain.content.dto.req.ContentUpdateReq;
import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.dto.res.MyContentRes;
import geulsam.archive.domain.content.dto.res.RecentContentRes;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.service.ContentService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/content")
@Validated
public class ContentController {

    private final ContentService contentService;

    /**
     * DB에 있는 모든 작품 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param genre 검색할 콘텐츠 객체의 genre
     * @param keyword 검색할 콘텐츠 객체의 제목 혹은 작가명 관련 문자열
     * @return PageRes<ContentRes> 생성된 순서대로 정렬된 콘텐츠 목록을 포함하는 페이지 결과
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<ContentRes>>> getContents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("createdAt").descending());

        PageRes<ContentRes> contentResList = contentService.getContents(genre, keyword, pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<ContentRes>>builder()
                        .data(contentResList)
                        .message("contents retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 특정 작품의 세부 정보 조회
     * @param id 조회할 콘텐츠의 고유 ID
     * @return ContentInfoRes 해당 id를 가진 콘텐츠의 정보를 포함한 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ContentInfoRes>> getContentInfo(@PathVariable String id) {

        ContentInfoRes contentInfoRes = contentService.getContentInfo(id);

        return ResponseEntity.ok().body(
                SuccessResponse.<ContentInfoRes>builder()
                        .data(contentInfoRes)
                        .message("content retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 최근 공개된 8개 작품 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @return PageRes<RecentContentRes> 가장 최근에 생성된 8개의 콘텐츠 목록을 포함하는 페이지 결과
     */
    @GetMapping("/recent")
    public ResponseEntity<SuccessResponse<PageRes<RecentContentRes>>> getRecentContents(
            @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page-1, 8, Sort.by("createdAt").descending());

        PageRes<RecentContentRes> recentContentResList = contentService.getRecentContents(pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<RecentContentRes>>builder()
                        .data(recentContentResList)
                        .message("recent contents retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );

    }

    /**
     * 로그인된 유저의 모든 작품 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @return PageRes<MyContentRes> 로그인된 유저의 생성된 순서대로 정렬된 콘텐츠 목록을 포함하는 페이지 결과
     */
    @GetMapping("/mine")
    public ResponseEntity<SuccessResponse<PageRes<MyContentRes>>> getMyContents(
            @RequestParam(defaultValue = "1") int page
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(page-1, 13, Sort.by("createdAt").descending());

        PageRes<MyContentRes> myContentResList = contentService.getMyContents(pageable, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<MyContentRes>>builder()
                        .data(myContentResList)
                        .message("user's contents retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * 작품 등록
     * @param contentUploadReq Content 객체 생성에 필요한 정보가 담긴 DTO
     * @return UUID 저장한 Content 객체의 고유 ID
     */
    @PostMapping()
    public ResponseEntity<SuccessResponse<UUID>> upload(@ModelAttribute @Valid ContentUploadReq contentUploadReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UUID contentId = contentService.upload(contentUploadReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<UUID>builder()
                        .data(contentId)
                        .status(HttpStatus.OK.value())
                        .message("content added successfully")
                        .build()
        );
    }

    /**
     * 작품 삭제
     * @param contentId 삭제하고 싶은 Content 객체의 고유 ID
     * @return null
     */
    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(@RequestParam(defaultValue = "id") String contentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        contentService.delete(contentId, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("content removed successfully")
                        .build()
        );
    }

    /**
     * 작품 수정
     * @param contentId 수정할 Content 객체의 고유 ID
     * @param contentUpdateReq Content 객체 수정에 필요한 정보가 담긴 DTO
     * @return ContentInfoRes 수정한 Content 객체의 정보를 포함한 DTO
     */
    @PutMapping()
    public ResponseEntity<SuccessResponse<ContentInfoRes>> update(
            @RequestParam(defaultValue = "id") String contentId,
            @RequestBody ContentUpdateReq contentUpdateReq
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ContentInfoRes updatedContentRes = contentService.update(contentId, contentUpdateReq, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<ContentInfoRes>builder()
                        .data(updatedContentRes)
                        .status(HttpStatus.OK.value())
                        .message("작품 수정 성공")
                        .build()
        );
    }
}
