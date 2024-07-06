package geulsam.archive.domain.content.service;

import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
                    UUID contentId = content.getId();
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

    @Transactional(readOnly = true)
    public ContentInfoRes getContentInfo(Integer id) {

        Content findContent = contentRepository.findById(id).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        return new ContentInfoRes(
                findContent.getId().toString(),
                findContent.getGenre(),
                findContent.getName(),
                findContent.getUser().getName(),
                findContent.getUser().getId().toString(),
                findContent.getCreatedAt(),
                //sentence?
                findContent.getPdfUrl()
        );
    }
}
