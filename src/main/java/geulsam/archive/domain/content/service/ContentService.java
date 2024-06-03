package geulsam.archive.domain.content.service;

import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ContentService {

    private ContentRepository contentRepository;
    private ContentAwardRepository contentAwardRepository;

    public List<ContentRes> getContents(Pageable pageable) {
        List<Content> findContents = contentRepository.findAll(pageable).getContent();
        return IntStream.range(0, findContents.size())
                .mapToObj(i -> {
                    Content content = findContents.get(i);
                    int contentId = content.getId();
                    ContentAward contentAward = contentAwardRepository.getReferenceById(contentId);
                    return new ContentRes(
                            contentId,
                            content.getUrl(),
                            content.getGenre(),
                            content.getName(),
                            contentAward.getContentAwardAt(),   //
                            content.getUser().getName(),
                            content.getCreatedAt()
                    );
                }).collect(Collectors.toList());
    }
}
