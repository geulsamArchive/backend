package geulsam.archive.domain.calendar.service;

import geulsam.archive.domain.calendar.dto.req.CalendarUpdateReq;
import geulsam.archive.domain.calendar.dto.req.CalendarUploadReq;
import geulsam.archive.domain.calendar.dto.req.CriticismUploadReq;
import geulsam.archive.domain.calendar.dto.req.RegularCriticismUploadReq;
import geulsam.archive.domain.calendar.dto.res.*;
import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.calendar.repository.CalendarRepository;
import geulsam.archive.domain.calendar.repository.CriticismRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CriticismRepository criticismRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void calendarUpload(CalendarUploadReq calendarUploadReq) {

        if(calendarRepository.existsOverlappingSchedule(calendarUploadReq.getStart(), calendarUploadReq.getEnd())){
            throw new ArchiveException(ErrorCode.VALUE_ERROR,"일정이 겹칩니다.");
        }

        Calendar calendar = new Calendar(
                calendarUploadReq.getTitle(),
                calendarUploadReq.getStart(),
                calendarUploadReq.getEnd(),
                calendarUploadReq.getLocate(),
                calendarUploadReq.getIntroduce()
        );

        calendarRepository.save(calendar);
    }

    @Transactional
    public void regularCriticismUpload(RegularCriticismUploadReq regularCriticismUploadReq){
        LocalDate currentDate = regularCriticismUploadReq.getStart();

//        String title = regularCriticismUploadReq.getStart().getYear() + " 정규합평";
//
//        if(regularCriticismUploadReq.getStart().getMonth().getValue() == 3){
//            title = regularCriticismUploadReq.getStart().getYear() + " 1학기" + " 정규합평";
//        } else if (regularCriticismUploadReq.getStart().getMonth().getValue() == 9) {
//            title = regularCriticismUploadReq.getStart().getYear() + " 2학기" + " 정규합평";
//        }

        while(!currentDate.isAfter(regularCriticismUploadReq.getEnd())){

            for(DayOfWeek dayOfWeek : regularCriticismUploadReq.getWeeks()){
                if(currentDate.getDayOfWeek() == dayOfWeek){

                    // 합평회 시작 시간
                    LocalDateTime startLocalDateTime = LocalDateTime.of(currentDate, regularCriticismUploadReq.getFirstTime());
                    // 합평회 끝 시간
                    LocalDateTime endLocalDateTime = LocalDateTime.of(currentDate, regularCriticismUploadReq.getSecondTime().plusHours(1));


                    if(calendarRepository.existsOverlappingSchedule(startLocalDateTime, endLocalDateTime)){
                        throw new ArchiveException(ErrorCode.VALUE_ERROR,"일정이 겹칩니다.");
                    }


                    Criticism criticism = new Criticism(
                            "합평회",
                            startLocalDateTime,
                            endLocalDateTime,
                            regularCriticismUploadReq.getLocation(),
                            "정규 합평회",
                            2
                    );

                    criticismRepository.save(criticism);
                }
            }

            currentDate = currentDate.plusDays(1);
        }
    }

    @Transactional
    public void calendarDelete(String field, String search) {
        String queryString = "DELETE FROM Calendar c WHERE c." + field + " = :search";
        entityManager.createQuery(queryString)
                .setParameter("search", search)
                .executeUpdate();
    }

    @Transactional
    public List<CalendarRes> calendar(int year, int semester) {

        // 기간 학기 단위로 설정
        LocalDateTime start;
        LocalDateTime end;

        if (semester == 1) {
            // 1학기: 3월 1일부터 8월 31일까지
            start = LocalDateTime.of(year, 3, 1, 0, 0);
            end = LocalDateTime.of(year, 8, 31, 23, 59, 59);
        } else if (semester == 2) {
            // 2학기: 9월 1일부터 다음 해 2월 말일까지
            LocalDate lastDayOfFebruary = LocalDate.of(year + 1, 2, 1).with(TemporalAdjusters.lastDayOfMonth()); // 윤년 고려하여 2월 마지막 날 계산
            start = LocalDateTime.of(year, 9, 1, 0, 0);
            end = LocalDateTime.of(lastDayOfFebruary, LocalTime.MAX);  // 2월 마지막 날의 23:59:59
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "1학기는 1, 2학기는 2를 입력하십시오");
        }

        List<Calendar> calendars =calendarRepository.calendarBySemester(start, end);

        // criticism 선언해서 바꾸기
        List<Criticism> criticisms = new ArrayList<>();

        for (Calendar calendar : calendars) {
            if (calendar instanceof Criticism criticism) {
                criticisms.add(criticism);
            } else {
                criticisms.add(new Criticism(calendar));
            }
        }

        List<CalendarRes> CalenderResList;

        // 1학기와 2학기를 나누어서 열 설정
        if(semester == 1){
            CalenderResList = IntStream.range(1, 7)
                    // 1학기 월 설정 (3-8)
                    .mapToObj(i -> new CalendarRes(i, year, i+2)).toList();
        } else {
            CalenderResList = IntStream.range(1, 7)
                    // 2학기 월 설정 (9-다음해 2)
                    .mapToObj(i -> new CalendarRes(i, year, (i + 7) % 12 + 1)).toList();
        }
        
        //CalendarRes 배열 담을 객체 생성


        // CalendarResList 의 12개 원소에 Calendars 의 각 달에 있는 이벤트를 배분
        for(CalendarRes calendarRes : CalenderResList){
            for(Criticism criticism : criticisms){
                int startMonth = criticism.getStart().getMonth().getValue();
                int endMonth = criticism.getEnd().getMonth().getValue();
                if(calendarRes.getMonthValue() == startMonth || calendarRes.getMonthValue() == endMonth){ // 이번달에 있는 이벤트면 추가
                    calendarRes.getEvents().add(new CalendarEvent(criticism));
                }
            }
            calendarRes.sortCalendarEvent();
            calendarRes.addId();
        }


        return CalenderResList;
    }

    @Transactional
    public void criticismUpload(CriticismUploadReq criticismUploadReq) {

        if(calendarRepository.existsOverlappingSchedule(criticismUploadReq.getStart(), criticismUploadReq.getEnd())){
            throw new ArchiveException(ErrorCode.VALUE_ERROR,"일정이 겹칩니다.");
        }

        Criticism criticism = new Criticism(
                criticismUploadReq.getTitle(),
                criticismUploadReq.getStart(),
                criticismUploadReq.getEnd(),
                criticismUploadReq.getLocate(),
                criticismUploadReq.getIntroduce(),
                criticismUploadReq.getAuthorCnt()
        );

        criticismRepository.save(criticism);
    }

    @Transactional
    public List<CriticismByMonth> criticism(int year, int season) {

        String queryString = "SELECT c FROM Criticism c WHERE c.start BETWEEN :startDate AND :endDate";

        //학기 끝 기간 설정
        int endSeason = switch (season) {
            case 1 -> 2;
            case 3 -> 6;
            case 7 -> 8;
            case 9 -> 12;
            default -> throw new ArchiveException(ErrorCode.VALUE_ERROR,"유효한 학기 시작 달 아님. 유효한 학기 시작 달: 겨울학기(1), 1학기(3), 여름학기(7), 겨울학기(1)");
        };
        // 기간 학기 단위로 설정
        LocalDateTime startDate = LocalDateTime.of(year, season, 1, 0, 0, 0);

        // Use YearMonth to get the last day of the end month
        YearMonth endYearMonth = YearMonth.of(year, endSeason);
        LocalDateTime endDate = endYearMonth.atEndOfMonth().atTime(23, 59, 59);

        // 쿼리문 실행
        List<Criticism> resultList = entityManager.createQuery(queryString, Criticism.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        List<CriticismByMonth> criticismByMonths = IntStream.rangeClosed(season, endSeason)
                .mapToObj(i -> new CriticismByMonth(i-season, i)).toList();

        for(Criticism criticism : resultList){

            int index = criticismByMonths.get(criticism.getStart().getMonth().getValue() - season).getCriticismRes().size();

            criticismByMonths
                    // criticism 있는 달을 통해 CriticismByMonth 를 가져온다
                    .get(criticism.getStart().getMonth().getValue() - season)
                    // criticismByMonth 의 CriticismList 에 Criticism 객체를 CriticismRes 객체로 만들어 삽입
                    .getCriticismRes().add(new CriticismRes(criticism, index));
        }

        return criticismByMonths;
    }

    @Transactional
    public CalendarOneRes one(int search) {

        Criticism criticism = criticismRepository.findById(search)
                .orElseGet(() -> {
                    // If Criticism not found, find Calendar and create a new Criticism object
                    Calendar calendar = calendarRepository.findById(search)
                            .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 일정 없음"));
                    return new Criticism(calendar);
                });

        return new CalendarOneRes(criticism);
    }

    @Transactional
    public void update(int search, CalendarUpdateReq calendarUpdateReq) {

        criticismRepository.findById(search).ifPresentOrElse(
                criticism -> {
                    // Criticism 객체 업데이트
                    criticism.updateByCalendarUpdateReq(calendarUpdateReq);
                },
                () -> {
                    // Criticism 객체가 없으면 Calendar 객체를 업데이트
                    Calendar calendar = calendarRepository.findById(search)
                            .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 일정 없음"));
                    calendar.updateByCalendarUpdateReq(calendarUpdateReq);
                }
        );
    }
}