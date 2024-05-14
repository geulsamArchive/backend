package geulsam.archive.domain.poster.controller;

import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.service.PosterService;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/poster")
@Validated
public class PosterController {

    private final PosterService posterService;

    /**
     * DB 에 있는 모든 poster 를 return
     * @return List<PosterRes>
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<PosterRes>>> poster(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "asc") String order ){

        // order String 에 따라 내림차순/오름차순 선정
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //pageNumber 는 클라이언트에서 1로 넘어오지만 Spring 의 페이징 기능은 페이지가 0부터 시작
        // pageSize 는 12로 고정, 정렬 기준 속성도 year 로 고정
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by(direction, "year"));
        List<PosterRes> poster = posterService.poster(pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<List<PosterRes>>builder()
                        .data(poster)
                        .message("posters get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
