package geulsam.archive.domain.calendar.controller;

import geulsam.archive.domain.calendar.dto.req.CalendarUpdateReq;
import geulsam.archive.domain.calendar.dto.req.CalendarUploadReq;
import geulsam.archive.domain.calendar.dto.req.CriticismUploadReq;
import geulsam.archive.domain.calendar.dto.req.RegularCriticismUploadReq;
import geulsam.archive.domain.calendar.dto.res.CalendarOneRes;
import geulsam.archive.domain.calendar.dto.res.CalendarRes;
import geulsam.archive.domain.calendar.dto.res.CriticismByMonth;
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

    @PostMapping("/regularCriticism")
    public ResponseEntity<SuccessResponse<Void>> regularCriticismUpload(@RequestBody RegularCriticismUploadReq regularCriticismUploadReq){
        calendarService.regularCriticismUpload(regularCriticismUploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("정규 합평회 업로드 성공").build()
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

    @GetMapping("/criticism")
    public ResponseEntity<SuccessResponse<List<CriticismByMonth>>> criticism(
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int season
    ) {

        List<CriticismByMonth> criticism = calendarService.criticism(year, season);

        return ResponseEntity.ok().body(
                SuccessResponse.<List<CriticismByMonth>>builder()
                        .data(criticism)
                        .status(HttpStatus.OK.value())
                        .message(season + " 일정 목록").build()
        );
    }

    /**
     * 일정 정보 1개 return
     * @param search 필요한 id 정보
     * @return
     */
    @GetMapping("/one")
    public ResponseEntity<SuccessResponse<CalendarOneRes>> one(
            @RequestParam(defaultValue = "0") int search
    ) {

        CalendarOneRes calendarOneRes = calendarService.one(search);

        return ResponseEntity.ok().body(
                SuccessResponse.<CalendarOneRes>builder()
                        .data(calendarOneRes)
                        .status(HttpStatus.OK.value())
                        .message("id " + search + " 일정 정보")
                        .build()
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

    @PutMapping
    public ResponseEntity<SuccessResponse<Void>> put(
            @RequestParam(defaultValue = "0") int search,
            @RequestBody CalendarUpdateReq calendarUpdateReq
    ) {
        calendarService.update(search, calendarUpdateReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("일정 수정 성공").build()
        );
    }
}
