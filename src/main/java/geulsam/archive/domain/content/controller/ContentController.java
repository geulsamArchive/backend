package geulsam.archive.domain.content.controller;

import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.global.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContentController {

    /**
     * Content Controller
     */
    private final ContentRepository contentRepository;

    public ResponseEntity<SuccessResponse<List<ContentRes>>> getContentInfos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "novel") String field,
            @RequestParam String search) {  //content?page=1&field=”novel”&search=”샘”
        List<Content> contents = new ArrayList<>();

        contentRepository.findAll();
        return  ResponseEntity.ok().body(
                SuccessResponse.<List<ContentRes>>builder()

                        .build()
        );
    }
}
