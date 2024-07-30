package geulsam.archive.domain.award.service;

import geulsam.archive.domain.award.dto.req.AwardUploadReq;
import geulsam.archive.domain.award.dto.res.AwardRes;
import geulsam.archive.domain.award.entitiy.Award;
import geulsam.archive.domain.award.repository.AwardRepository;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
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

    @Transactional
    public Integer upload(AwardUploadReq awardUploadReq) {
        Award newAward = new Award(
                awardUploadReq.getName(),
                awardUploadReq.getExplain(),
                LocalDate.now()
        );

        Award savedAward = awardRepository.save(newAward);

        return savedAward.getId();
    }

    /**
     * 관련 Content의 award 필드를 null로 설정하고 해당 Award를 삭제한다.
     * @param id 삭제할 상의 id 값
     */
    @Transactional
    public void delete(int id) {
        Award award = awardRepository.findById(id).orElseThrow(
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
