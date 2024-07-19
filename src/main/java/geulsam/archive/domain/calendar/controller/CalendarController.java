package geulsam.archive.domain.calendar.controller;

import geulsam.archive.domain.calendar.dto.req.CalendarUploadReq;
import geulsam.archive.domain.calendar.dto.req.CriticismUploadReq;
import geulsam.archive.domain.calendar.dto.res.CalendarRes;
import geulsam.archive.domain.calendar.service.CalendarService;
import geulsam.archive.global.common.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/calendar")
@Validated
public class CalendarController {

    private final CalendarService calendarService;

    /**
     * 일정 생성
     * @param calendarUploadReq
     * @return
     */
    @PostMapping()
    public ResponseEntity<SuccessResponse<Void>> upload(@RequestBody @Valid CalendarUploadReq calendarUploadReq){

        calendarService.calendarUpload(calendarUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("일정 업로드 성공").build()
        );
    }

    /**
     * 합평회 생성
     * @param criticismUploadReq
     * @return
     */
    @PostMapping("/criticism")
    public ResponseEntity<SuccessResponse<Void>> criticismUpload(@RequestBody @Valid CriticismUploadReq criticismUploadReq){

        calendarService.criticismUpload(criticismUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("합평회 업로드 성공").build()
        );
    }

    /**
     * 일정 삭제
     * @param field 삭제할 기준 컬럼 이름
     * @param search 삭제할 기준 컬럼의 값
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam(defaultValue = "id") String field,
            @RequestParam String search

    ){
        calendarService.calendarDelete(field, search);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("일정 삭제 성공").build()
        );
    }

    /**
     *
     * @param field
     * @param search
     * @return
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<CalendarRes>>> calendar(
            @RequestParam(defaultValue = "start") String field,
            @RequestParam int search
    ){
        List<CalendarRes> calendar = calendarService.calendar(field, search);

        return ResponseEntity.ok().body(
                SuccessResponse.<List<CalendarRes>>builder()
                        .data(calendar)
                        .status(HttpStatus.OK.value())
                        .message(Integer.toString(search) + "년 일정 목록").build()
        );
    }
}
