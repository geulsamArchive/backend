package geulsam.archive.domain.criticismLog.service;

import geulsam.archive.domain.calendar.repository.CalendarRepository;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import geulsam.archive.domain.criticismAuthor.repository.CriticismAuthorRepository;
import geulsam.archive.domain.criticismLog.dto.req.AdminUploadReq;
import geulsam.archive.domain.criticismLog.repository.CriticismLogRepository;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CriticismLogService {

    private final CriticismLogRepository criticismLogRepository;
    private final ContentRepository contentRepository;
    private final CalendarRepository calendarRepository;
    private final CriticismAuthorRepository criticismAuthorRepository;
    private final UserRepository userRepository;

    /**
     * 관리자가 합평 기록 업로드하는 함수 POST
     * @param adminUploadReq
     */
    public void adminUpload(AdminUploadReq adminUploadReq) {

    }

    /**
     * 사용자가 직접 자기 작품 합평회 등록하는 함수 POST
     * @param uuid
     */
    public void userUpload(String uuid){

    }

    /**
     * 이미 업로드 된 합평 노트 정보 업로드 함수 POST
     */
    public void close() {

    }

    /**
     * 합평 기록 전체 검색해서 리턴하는 함수 GET
     */
    public void criticismLog(){

    }

    /**
     * 해당 합평회가 이미 있는지 검사하는 함수 GET
     */
    public void check(String contentID){

    }

    /**
     * 합평 기록 사전 업로드가 가능한 날짜 검색해서 리턴하는 함수
     * @param userID controller 에서 가져온 사용자 ID
     * @param contentID 넘겨받은 Content ID
     */
    public void availableDate(Integer userID, String contentID){
        Content content = contentRepository.findById(UUID.fromString(contentID)).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당하는 작품 없음")
        );
        List<CriticismAuthor> availableDateTime = criticismAuthorRepository.findAvailableDateTime(userID,content.getGenre(), LocalDateTime.now());

        System.out.println(availableDateTime);
    }
}
