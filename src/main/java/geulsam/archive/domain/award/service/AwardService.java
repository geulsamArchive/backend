package geulsam.archive.domain.award.service;

import geulsam.archive.domain.award.dto.req.AwardUploadReq;
import geulsam.archive.domain.award.dto.res.AwardRes;
import geulsam.archive.domain.award.entitiy.Award;
import geulsam.archive.domain.award.repository.AwardRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final ContentAwardRepository contentAwardRepository;
    private final UserRepository userRepository;

    /**
     * Award 전체를 리턴하는 트랜잭션
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<AwardRes>
     */
    @Transactional(readOnly = true)
    public PageRes<AwardRes> getAwards(Pageable pageable) {
        Page<Award> awardPage = awardRepository.findAll(pageable);

        List<AwardRes> awardResList = awardPage.getContent().stream()
                .map(AwardRes::new)
                .collect(Collectors.toList());

        return new PageRes<>(
                awardPage.getTotalPages(),
                awardResList
        );
    }

    @Transactional(readOnly = true)
    public PageRes<AwardRes> getAwardsByYear(int year, Pageable pageable) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        Page<Award> awardPage = awardRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(startDate, endDate, pageable);

        List<AwardRes> awardResList = awardPage.getContent().stream()
                .map(AwardRes::new)
                .collect(Collectors.toList());

        return new PageRes<>(
                awardPage.getTotalPages(),
                awardResList
        );
    }

    /**
     * Award 를 생성하는 트랜잭션
     * Level.GRADUATED와 Level.SUSPENDED는 해당 권한이 없다.
     * @param awardUploadReq Award 객체 생성에 필요한 정보가 담긴 DTO
     * @param userId 로그인한 유저의 ID
     * @return Integer 생성한 Award 의 ID
     */
    @Transactional
    public Integer upload(AwardUploadReq awardUploadReq, int userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        if(findUser.getLevel().equals(Level.GRADUATED) || findUser.getLevel().equals(Level.SUSPENDED)) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "사용자 권한 없음");
        }

        Award newAward = new Award(
                awardUploadReq.getName(),
                awardUploadReq.getExplain(),
                LocalDate.now()
        );

        Award savedAward = awardRepository.save(newAward);

        return savedAward.getId();
    }

    /**
     * 특정 Award 를 삭제하는 트랜잭션
     * 수상한 콘텐츠의 award 필드를 null로 설정하고 해당 award id를 가진 상을 삭제한다.
     * Level.GRADUATED와 Level.SUSPENDED는 해당 권한이 없다.
     * @param awardId 삭제할 상의 id 값
     * @param userId 로그인한 유저의 id
     */
    @Transactional
    public void delete(int awardId, int userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        if(findUser.getLevel().equals(Level.GRADUATED) || findUser.getLevel().equals(Level.SUSPENDED)) {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "사용자 권한 없음");
        }

        Award award = awardRepository.findById(awardId).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 award 없음")
        );

        List<ContentAward> contentAwardList = contentAwardRepository.findByAward(award);
        for(ContentAward contentAward : contentAwardList) {
            contentAward.changeAward(null);
        }

        contentAwardRepository.saveAll(contentAwardList);

        awardRepository.deleteById(award.getId());
    }
}
