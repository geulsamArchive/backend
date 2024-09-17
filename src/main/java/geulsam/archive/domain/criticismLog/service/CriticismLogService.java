package geulsam.archive.domain.criticismLog.service;

import geulsam.archive.domain.calendar.repository.CalendarRepository;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.criticismAuthor.repository.CriticismAuthorRepository;
import geulsam.archive.domain.criticismLog.dto.req.UploadReq;
import geulsam.archive.domain.criticismLog.dto.res.CriticismLogRes;
import geulsam.archive.domain.criticismLog.entity.CriticismLog;
import geulsam.archive.domain.criticismLog.repository.CriticismLogRepository;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriticismLogService {

    private final CriticismLogRepository criticismLogRepository;

    /**
     * 관리자가 합평 기록 업로드하는 함수 POST
     * @param uploadReq
     */
    @Transactional
    public void upload(UploadReq uploadReq) {

        if(criticismLogRepository.existsCriticismLogBy(uploadReq.getUserName(), uploadReq.getContentTitle(), uploadReq.getLocalDate())){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "해당 합평 기록 이미 존재. 발표자, 제목, 발표일이 모두 달라야 함");
        }

        CriticismLog criticismLog = new CriticismLog(uploadReq);
        criticismLogRepository.save(criticismLog);
    }

    /**
     * 관리자가 합평 기록 삭제하는 함수
     * @param criticismLogId 지울 합평 ID
     */
    public void delete(String criticismLogId){
        try{
            UUID uuid = UUID.fromString(criticismLogId);
            criticismLogRepository.deleteById(uuid);
        } catch (IllegalArgumentException e){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "criticismLogId 가 uuid 변환 불가 형식");
        } catch (EmptyResultDataAccessException e){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "지울 객체가 없음");
        }
    }

    /**
     * 합평 기록 전체 검색해서 리턴하는 함수 GET
     */
    public PageRes<CriticismLogRes> criticismLog(Pageable pageable, String keyword){
        Page<CriticismLog> criticismLogPage;

        if (keyword == null || keyword.isEmpty()) {
            criticismLogPage = criticismLogRepository.findAll(pageable); // 전체 데이터를 가져오는 로직
        } else {
            criticismLogPage = criticismLogRepository.findCriticismLogByFilters(pageable, keyword); // 필터된 데이터를 가져오는 로직
        }

        AtomicInteger index = new AtomicInteger(0);
        List<CriticismLogRes> criticismLogResList = criticismLogPage.getContent().stream()
                .map(criticismLog -> new CriticismLogRes(criticismLog, index.getAndIncrement()))
                .toList();

        return new PageRes<>(
                criticismLogPage.getTotalPages(),
                criticismLogResList
        );
    }
}
