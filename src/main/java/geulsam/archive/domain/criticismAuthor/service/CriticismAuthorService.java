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
    public void delete(Integer userId, int search, String roles) {
        CriticismAuthor criticismAuthor = criticismAuthorRepository.findById(search).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "지우려는 합평회 신청 없음")
        );

        // 유저의 역할이 NORMAL 이고 지우고자 하는 criticismAuthor 객체의 userID와 같으며 해당 객체의 상태가 UNFIXED 일때
        if (roles.equals("ROLE_NORMAL") && Objects.equals(criticismAuthor.getAuthor().getId(), userId) && criticismAuthor.getCondition() == Condition.UNFIXED) {
                criticismAuthorRepository.deleteById(search);
        } else if (roles.equals("ROLE_ADMIN")) {
            criticismAuthorRepository.deleteById(search);
        } else {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR);
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
