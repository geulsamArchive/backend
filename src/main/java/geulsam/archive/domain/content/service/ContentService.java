package geulsam.archive.domain.content.service;

import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentAwardRepository contentAwardRepository;
    @Transactional(readOnly = true)
    public List<ContentRes> getContents(Pageable pageable) {
        List<Content> findContents = contentRepository.findAll(pageable).getContent();
        System.out.println(findContents);
        return IntStream.range(0, findContents.size())
                .mapToObj(i -> {
                    Content content = findContents.get(i);
                    int contentId = content.getId();
                    //ContentAward contentAward = contentAwardRepository.findById(contentId).orElseThrow(RuntimeException::new);  //API 명세서 확인 필요
                    return new ContentRes(
                            i,
                            content.getId().toString(),
                            content.getGenre(),
                            content.getName(),
                            2024,//contentAward.getContentAwardAt(),   //
                            content.getUser().getName(),
                            content.getCreatedAt()
                    );
                }).collect(Collectors.toList());
    }
}
