package geulsam.archive.domain.criticismAuthor.service;

import geulsam.archive.domain.criticismAuthor.dto.req.CriticismAuthorCloseReq;
import geulsam.archive.domain.criticismAuthor.dto.res.CriticismAuthorRes;
import geulsam.archive.domain.calendar.entity.Criticism;
import geulsam.archive.domain.calendar.repository.CriticismRepository;
import geulsam.archive.domain.criticismAuthor.dto.req.CriticismAuthorUploadReq;
import geulsam.archive.domain.criticismAuthor.entity.Condition;
import geulsam.archive.domain.criticismAuthor.entity.CriticismAuthor;
import geulsam.archive.domain.criticismAuthor.repository.CriticismAuthorRepository;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriticismAuthorService {

    private final CriticismRepository criticismRepository;
    private final UserRepository userRepository;
    private final CriticismAuthorRepository criticismAuthorRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void upload(CriticismAuthorUploadReq criticismAuthorUploadReq, int userId) {
        Criticism criticism = criticismRepository.findById(criticismAuthorUploadReq.getCriticismId())
                .orElseThrow(() ->new ArchiveException(ErrorCode.VALUE_ERROR, "해당 합평회 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 유저 없음"));

        // 순서는 합평회 인원수보다  작아야 함
        if(criticism.getAuthorCnt() < criticismAuthorUploadReq.getOrder()){
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "신청할 수 있는 인원수 초과");
        }

        // 타입의 신청 여부 검사
        for(CriticismAuthor criticismAuthor : criticism.getCriticismAuthors()){
            if(criticismAuthor.getOrder() == criticismAuthorUploadReq.getOrder()){
                throw new ArchiveException(ErrorCode.VALUE_ERROR, "이미 타인이 신청했습니다.");
            }
        }

        // 새로운 criticism-author 객체 생성
        CriticismAuthor criticismAuthor = new CriticismAuthor(
                user,
                criticism,
                criticismAuthorUploadReq.getOrder(),
                criticismAuthorUploadReq.getGenre()
        );

        criticism.addCriticismAuthor(criticismAuthor);

        criticismAuthorRepository.save(criticismAuthor);
    }

    @Transactional
    public void delete(Integer userId, int search, int order, String roles) {
        Criticism criticism = criticismRepository.findById(search)
                .orElseThrow(() ->new ArchiveException(ErrorCode.VALUE_ERROR, "해당 합평회 없음"));

        // 합평회의 신청 리스트 전체를 순회
        boolean deleted = false;

        Iterator<CriticismAuthor> iterator = criticism.getCriticismAuthors().iterator();
        while (iterator.hasNext()) {
            // 계속 다음 탐색
            CriticismAuthor criticismAuthor = iterator.next();
            // 합평회 순서가 입력받은 순서 숫자와 일치한다면
            if (criticismAuthor.getOrder() == order) {
                // 해당 합평회의 작가 아이디와 입력받은 아이디가 일치하거나 관리자 권한이라면
                if (criticismAuthor.getAuthor().getId().intValue() == userId.intValue() ||
                        Objects.equals(roles, "ROLE_ADMIN")) {
                    // criticism 과의 연관관계 제거
                    criticism.removeCriticismAuthor(criticismAuthor);
                    // 데이터베이스에서 삭제
                    criticismAuthorRepository.deleteById(criticismAuthor.getId());
                    criticism.removeCriticismAuthor(criticismAuthor);
                    // Remove from the collection
                    iterator.remove();
                    deleted = true; // Set flag indicating deletion occurred
                }
            }
        }

        if (!deleted) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR,"지우려는 신청이 없음");
        }
    }

    @Transactional
    public Condition toggle(int search){
        CriticismAuthor criticismAuthor = criticismAuthorRepository.findById(search).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 신청 없음")
        );
        criticismAuthor.toggleCondition();

        return criticismAuthor.getCondition();
    }

    @Transactional(readOnly = true)
    public PageRes<CriticismAuthorRes> criticismAuthor(Pageable pageable) {
        Page<CriticismAuthor> criticismAuthorPages = criticismAuthorRepository.findCriticismAuthorBeforeNow(LocalDateTime.now(), pageable);

        List<CriticismAuthorRes> criticismAuthorRes = criticismAuthorPages.getContent().stream()
                .map(criticismAuthor -> new CriticismAuthorRes(criticismAuthor, criticismAuthorPages.getContent().indexOf(criticismAuthor))).toList();

        return new PageRes<>(
                criticismAuthorPages.getTotalPages(),
                criticismAuthorRes
        );
    }

    @Transactional
    public void close(CriticismAuthorCloseReq criticismAuthorCloseReq) {
        CriticismAuthor criticismAuthor = criticismAuthorRepository.findByContentId(UUID.fromString(criticismAuthorCloseReq.getContentId())).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 신청 없음")
        );

        criticismAuthor.close(criticismAuthorCloseReq);
    }
}
