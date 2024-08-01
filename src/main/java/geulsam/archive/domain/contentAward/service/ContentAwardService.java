package geulsam.archive.domain.contentAward.service;

import geulsam.archive.domain.contentAward.dto.res.ContentAwardRes;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import geulsam.archive.global.common.dto.PageRes;
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
public class ContentAwardService {
    private final ContentAwardRepository contentAwardRepository;

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
}
