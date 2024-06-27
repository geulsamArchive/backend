package geulsam.archive.domain.poster.service;

import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.entity.Poster;
import geulsam.archive.domain.poster.repository.PosterRepository;
import geulsam.archive.global.s3.PreSignedUrlManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepository posterRepository;
    private final PreSignedUrlManager preSignedUrlManager;

    /**
     * Poster 전체를 검색해서 PosterRes 배열로 반환
     * @return List
     */
    @Transactional(readOnly = true)
    public PageRes<PosterRes> poster(Pageable pageable) {
        Page<Poster> posterPage = posterRepository.findAll(pageable);

        //Poster 객체를 PosterRes 객체로 mapping
        List<PosterRes> posterResList = posterPage.getContent().stream()
                .map(poster -> new PosterRes(poster, posterPage.getContent().indexOf(poster)))
                .collect(Collectors.toList());

        return new PageRes<>(
                posterPage.getTotalPages(),
                posterResList
        );
    }
}
