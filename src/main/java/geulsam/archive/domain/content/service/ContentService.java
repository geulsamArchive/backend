package geulsam.archive.domain.content.service;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentAwardRepository contentAwardRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * Content 전체를 리턴하는 트랜잭션
     * @param field Content 객체의 genre 검색 문자열
     * @param search Content 객체의 title 검색 문제열
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<ContentRes> 페이지네이션 정보와 ContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional(readOnly = true)
    public PageRes<ContentRes> getContents(String field, String search, Pageable pageable) {
        Page<Content> contentPage;
        Genre genre = null;
        if (field != null) {
            try {
                genre = Genre.valueOf(field.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ArchiveException(ErrorCode.VALUE_ERROR, "Invalid category: " + field);
            }
        }

        if (genre != null && search != null) {
            contentPage = contentRepository.findByGenreAndNameContaining(genre, search, pageable);

        } else if (genre != null) {
            contentPage = contentRepository.findByGenre(genre, pageable);

        } else if (search != null) {
            contentPage = contentRepository.findByNameContaining(search, pageable);

        } else {
            contentPage = contentRepository.findAll(pageable);
        }

        List<ContentRes> contentResList = contentPage.getContent().stream()
                .map(content -> {
                    List<ContentAward> awards = contentAwardRepository.findByContentId(content.getId());
                    return new ContentRes(content, contentPage.getContent().indexOf(content), awards);
                })
                .collect(Collectors.toList());

        return new PageRes<>(
                contentPage.getTotalPages(),
                contentResList
        );
    }

    /**
     * Content 객체의 Id 1개를 받아 Content 의 세부사항을 리턴하는 트랜잭션
     * @param contentId Content 객체의 id
     * @return ContentInfoRes
     */
    @Transactional(readOnly = true)
    public ContentInfoRes getContentInfo(String contentId) {

        Content findContent = contentRepository.findById(UUID.fromString(contentId)).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        return new ContentInfoRes(findContent);
    }

    @Transactional
    public UUID upload(ContentUploadReq contentUploadReq) {
        User findUser = userRepository.findById(contentUploadReq.getUserId()).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        Optional<Book> findBook = bookRepository.findById(contentUploadReq.getBookId());

        Content newContent = new Content(
                findUser,
                findBook.orElse(null),
                contentUploadReq.getName(),
                contentUploadReq.getPdfUrl(),
                contentUploadReq.getHtmlUrl(),
                contentUploadReq.getGenre(),
                LocalDateTime.now(),
                contentUploadReq.getIsVisible(),
                contentUploadReq.getBookPage(),
                contentUploadReq.getSentence()
        );

        Content savedContent = contentRepository.save(newContent);

        return savedContent.getId();
    }
}
