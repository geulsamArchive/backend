package geulsam.archive.domain.content.service;

import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentAwardRepository contentAwardRepository;
    @Transactional(readOnly = true)
    public PageRes<ContentRes> getContents(String field, String search, Pageable pageable) {
        Page<Content> contentPage;
        Genre genre = field != null ? Genre.valueOf(field.toUpperCase()) : null;

        if (genre != null && search != null) {
            contentPage = contentRepository.findByGenreAndTitleContaining(genre, search, pageable);

        } else if (genre != null) {
            contentPage = contentRepository.findByGenre(genre, pageable);

        } else if (search != null) {
            contentPage = contentRepository.findByTitleContaining(search, pageable);

        } else {
            contentPage = contentRepository.findAll(pageable);
        }

        List<ContentRes> contentResList = contentPage.getContent().stream()
                .map(content -> new ContentRes(content, contentPage.getContent().indexOf(content)))
                .collect(Collectors.toList());

        return new PageRes<>(
                contentPage.getTotalPages(),
                contentResList
        );
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
