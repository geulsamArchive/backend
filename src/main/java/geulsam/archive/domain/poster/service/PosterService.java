package geulsam.archive.domain.poster.service;

import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.entity.Poster;
import geulsam.archive.domain.poster.repository.PosterRepository;
import geulsam.archive.global.s3.PreSignedUrlManager;
import lombok.RequiredArgsConstructor;
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
    public List<PosterRes> poster() {
        List<Poster> posterList = posterRepository.findAll();

        return IntStream.range(0, posterList.size())
                .mapToObj(i -> {
                    Poster poster = posterList.get(i);
                    return new PosterRes(
                            i,
                            poster.getUrl(),
                            poster.getThumbNailUrl(),
                            poster.getDesigner(),
                            poster.getYear().getValue(),
                            poster.getCreatedAt()
                    );
                }).collect(Collectors.toList());
    }
}
