package geulsam.archive.domain.criticismLog.controller;

import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.criticismLog.dto.req.UploadReq;
import geulsam.archive.domain.criticismLog.dto.res.CriticismLogRes;
import geulsam.archive.domain.criticismLog.service.CriticismLogService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import geulsam.archive.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/criticismLog")
public class CriticismLogController {

    private final CriticismLogService criticismLogService;

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "합평 기록 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> upload(@RequestBody UploadReq uploadReq){
        criticismLogService.upload(uploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("합평 기록 업로드 성공")
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam String id
    ){
        criticismLogService.delete(id);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("합평 기록 삭제 성공")
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<CriticismLogRes>>> criticismLog(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword
    ){
        Pageable pageable = PageRequest.of(page-1, 12, Sort.by("localDate").descending());

        PageRes<CriticismLogRes> criticismLogResPageRes = criticismLogService.criticismLog(pageable, keyword);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<CriticismLogRes>>builder()
                        .data(criticismLogResPageRes)
                        .message("CriticismLog get Success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
