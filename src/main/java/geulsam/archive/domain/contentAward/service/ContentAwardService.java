package geulsam.archive.domain.contentAward.service;

import geulsam.archive.domain.award.entitiy.Award;
import geulsam.archive.domain.award.repository.AwardRepository;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.dto.req.PresentAwardReq;
import geulsam.archive.domain.contentAward.dto.res.ContentAwardRes;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import geulsam.archive.domain.user.entity.Level;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentAwardService {
    private final ContentAwardRepository contentAwardRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final AwardRepository awardRepository;

    /**
     * startDate부터 endDate까지의 기간까지의 수상 내역을 리턴하는 트랜잭션
     * @param startDate 시작 날짜(yyyy-MM-dd 형태)
     * @param endDate 끝 날짜(yyyy-MM-dd 형태)
     * @param pageable pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<ContentAwardRes> 페이지네이션 정보와 ContentAward 객체 리스트를 포함하는 ContentAwardRes 객체
     */
    @Transactional(readOnly = true)
    public PageRes<ContentAwardRes> getAwardsByPeriod(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<ContentAward> contentAwardPage = contentAwardRepository.findByContentAwardAtGreaterThanEqualAndContentAwardAtLessThanEqual(startDate, endDate, pageable);

        List<ContentAwardRes> contentAwardResList = contentAwardPage.getContent().stream()
                .map(ContentAwardRes::new)
                .collect(Collectors.toList());

        return new PageRes<>(
                contentAwardPage.getTotalPages(),
                contentAwardResList
        );
    }

    /**
     * 특정 Content 에 Award 를 부여하는 트랜잭션
     * LEVEL.ADMIN 타입을 가진 User 만이 실행 가능하다.
     * @param presentAwardReq Award 수상에 필요한 정보가 담긴 DTO
     * @param userId 로그인한 유저의 id
     */
    @Transactional
    public void presentAward(PresentAwardReq presentAwardReq, Integer userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        if(findUser.getLevel().equals(Level.ADMIN)) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "사용자 권한 없음");
        }

        Content findContent = contentRepository.findById(UUID.fromString(presentAwardReq.getContentId())).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        Award findAward = awardRepository.findById(presentAwardReq.getAwardId()).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Award 없음"
        ));

        ContentAward newContentAward = new ContentAward(
                findContent,
                findAward,
                findUser,
                presentAwardReq.getPresentAt()
        );

        contentAwardRepository.save(newContentAward);
    }
}
