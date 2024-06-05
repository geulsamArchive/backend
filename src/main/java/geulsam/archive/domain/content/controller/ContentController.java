package geulsam.archive.domain.content.controller;

import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.content.service.ContentService;
import geulsam.archive.global.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/content")
public class ContentController {

    /**
     * Content Controller
     */
    private final ContentRepository contentRepository;
    private final ContentService contentService;

    /**
     * 작품 조회 API
     * DB에 있는 모든 작품 리턴
     * @return List<ContentRes>
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<ContentRes>>> getContents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "novel") String field,
            @RequestParam String search) {  //content?page=1&field=”novel”&search=”샘”

        Pageable pageable = PageRequest.of(page-1, 3);  //, Sort.by("createdAt")
        List<ContentRes> contentResList = contentService.getContents(pageable);

        return  ResponseEntity.ok().body(
                SuccessResponse.<List<ContentRes>>builder()
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
    public ResponseEntity<SuccessResponse<ContentInfoRes>> getContentInfo(@PathVariable Integer id) {


        ContentInfoRes contentInfoRes = contentService.getContentInfo(id);

        //contentInfoRes 채우기

        return ResponseEntity.ok().body(
                SuccessResponse.<ContentInfoRes>builder()
                        .data(contentInfoRes)
                        .message("contents get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
