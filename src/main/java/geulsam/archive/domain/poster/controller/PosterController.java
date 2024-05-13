package geulsam.archive.domain.poster.controller;

import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.service.PosterService;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @return ResponseEntity
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<PosterRes>>> poster(){

        List<PosterRes> poster = posterService.poster();

        return ResponseEntity.ok().body(
                SuccessResponse.<List<PosterRes>>builder()
                        .data(poster)
                        .message("posters get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
