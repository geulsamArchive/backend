package geulsam.archive.domain.content.controller;

import geulsam.archive.domain.content.dto.req.ContentUpdateReq;
import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.*;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.service.ContentService;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import geulsam.archive.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/content")
@Validated
public class ContentController {

    private final ContentService contentService;
    private final UserRepository userRepository;

    /**
     * 조건을 만족하는 모든 작품 조회
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param genre 검색할 콘텐츠 객체의 genre
     * @param keyword 검색할 콘텐츠 객체의 제목 혹은 작가명 관련 문자열
     * @return PageRes<ContentRes> 생성된 순서대로 정렬된 콘텐츠 목록을 포함하는 페이지 결과
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<ContentRes>>> getContentsByFilters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("createdAt").descending());

        PageRes<ContentRes> contentResList;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            //비로그인 사용자인 경우
            contentResList = contentService.getContentsByFiltersForANONYMOUS(genre, keyword, pageable);

        } else if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails){
            User findUser = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new ArchiveException(
                    ErrorCode.VALUE_ERROR, "해당 User 없음"
            ));

            contentResList = switch (findUser.getLevel()) {
                case SUSPENDED -> contentService.getContentsByFiltersForANONYMOUS(genre, keyword, pageable);
                case NORMAL, ADMIN -> contentService.getContentsByFilters(genre, keyword, pageable);
                default -> throw new ArchiveException(ErrorCode.VALUE_ERROR, "지원하지 않는 타입: " + findUser.getLevel());
            };

        } else {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "잘못된 인증 타입");
        }

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<ContentRes>>builder()
                        .data(contentResList)
                        .message("contents retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * book 에 수록하기 위한 content 의 id(목록) 추출
     * @param userName content 제작 작가의 이름
     * @param contentTitle content 제목
     * @return contentId List String
     */
    @GetMapping("/forBook")
    public ResponseEntity<SuccessResponse<List<String>>> getContentForBook (
            @RequestParam String userName,
            @RequestParam String contentTitle
    ) {

        List<String> contentsForBook = contentService.getContentForBook(userName, contentTitle);

        return ResponseEntity.ok().body(
                SuccessResponse.<List<String>>builder()
                        .data(contentsForBook)
                        .message("contents list for book successfully")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    };

    /**
     * 특정 작품의 세부 정보 조회
     * @param id 조회할 콘텐츠의 고유 ID
     * @return ContentInfoRes 해당 id를 가진 콘텐츠의 정보를 포함한 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<ContentInfoRes>> getContentInfo(@PathVariable String id) {

        ContentInfoRes contentInfoRes;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            contentInfoRes = contentService.getContentInfoForANONYMOUS(id);
        } else if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            contentInfoRes = contentService.getContentInfo(id, userDetails.getUserId());
        } else {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "잘못된 인증 타입");
        }

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
        PageRes<RecentContentRes> recentContentResList;

        Pageable pageable = PageRequest.of(page-1, 8, Sort.by("createdAt").descending());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            recentContentResList = contentService.getContentsForANONYMOUS(pageable);
        } else if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            recentContentResList = contentService.getContents(pageable, userDetails.getUserId());
        } else {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "잘못된 인증 타입");
        }

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
     * 특정 유저의 모든 작품 조회
     * 유저의 Level이 Normal이면 IsVisible.LOGGEDIN를, SUSPENDED이면 IsVisible.EVERY에 해당하는 Content 리스트를 제공함.
     * 로그인된 유저가 작가 본인일 시, IsVisible.PRIVATE에 해당하는 Content 리스트를 제공함.
     * @param page 조회할 페이지 번호 (기본값: 1)
     * @param authorId 조회할 유저의 id
     * @return PageRes<AuthorContentRes>> 특정 유저의 콘텐츠 목록을 포함하는 페이지 결과
     */
    @GetMapping("/author")
    public ResponseEntity<SuccessResponse<PageRes<AuthorContentRes>>> getAuthorContent(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam int authorId
    ) {
        PageRes<AuthorContentRes> authorContentResList;

        Pageable pageable = PageRequest.of(page-1, 8, Sort.by("createdAt").descending());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            authorContentResList = contentService.getAuthorContentForANONYMOUS(pageable, authorId);
        } else if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            authorContentResList = contentService.getAuthorContent(pageable, authorId, userDetails.getUserId());
        } else {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "잘못된 인증 타입");
        }

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<AuthorContentRes>>builder()
                        .data(authorContentResList)
                        .message("author's contents retrieved successfully")
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

    /**
     * 작품 수상
     * @param contentId 수상 받을 Content 객체의 고유 ID
     * @param award 수상될 상의 이름을 담은 문자열
     * @return String 수상된 상의 이름
     */
    @PutMapping("award")
    public ResponseEntity<SuccessResponse<String>> presentAward(
            @RequestParam(defaultValue = "id") String contentId,
            @RequestParam @NotNull String award
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String presentedAward = contentService.presentAward(contentId, award, userDetails.getUserId());

        return ResponseEntity.ok().body(
                SuccessResponse.<String>builder()
                        .data(presentedAward)
                        .status(HttpStatus.OK.value())
                        .message("작품 수상 성공")
                        .build()
        );
    }
}
