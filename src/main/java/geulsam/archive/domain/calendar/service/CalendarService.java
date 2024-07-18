package geulsam.archive.domain.calendar.service;

import geulsam.archive.domain.calendar.dto.req.CalendarUploadReq;
import geulsam.archive.domain.calendar.dto.req.CriticismUploadReq;
import geulsam.archive.domain.calendar.dto.res.CalendarEvent;
import geulsam.archive.domain.calendar.dto.res.CalendarRes;
import geulsam.archive.domain.calendar.entity.Calendar;
import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.calendar.repository.CalendarRepository;
import geulsam.archive.domain.calendar.repository.CriticismRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    public void calendarDelete(String field, String search) {
        String queryString = "DELETE FROM Calendar c WHERE c." + field + " = :search";
        entityManager.createQuery(queryString)
                .setParameter("search", search)
                .executeUpdate();
    }

    @Transactional
    public List<CalendarRes> calendar(String field, int search) {
        // 사용할 쿼리문
        String queryString = "SELECT c FROM Calendar c WHERE c.start BETWEEN :startDate AND :endDate";

        // 기간 1년 단위로 설정
        LocalDateTime startDate = LocalDateTime.of(search, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(search, 12, 31, 23, 59, 59);

        // 쿼리문 실행
        List<Calendar> calendars = entityManager.createQuery(queryString, Calendar.class)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .getResultList();

        // criticism 선언해서 바꾸기
        List<Criticism> criticisms = new ArrayList<>();

        for (Calendar calendar : calendars) {
            if (calendar instanceof Criticism criticism) {
                criticisms.add(criticism);
            } else {
                criticisms.add(new Criticism(calendar));
            }
        }
        
        //CalendarRes 배열 담을 객체 생성
        List<CalendarRes> CalenderResList = IntStream.rangeClosed(1, 12)
        .mapToObj(i -> new CalendarRes(i, search + "년 " + i + "월")).toList();

        // CalendarResList 의 12개 원소에 Calendars 의 각 달에 있는 이벤트를 배분
        for(CalendarRes calendarRes : CalenderResList){
            for(Criticism criticism : criticisms){
                int startMonth = criticism.getStart().getMonth().getValue();
                int endMonth = criticism.getEnd().getMonth().getValue();
                if(calendarRes.getId() == startMonth || calendarRes.getId() == endMonth){ // 이번달에 있는 이벤트면 추가
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
}