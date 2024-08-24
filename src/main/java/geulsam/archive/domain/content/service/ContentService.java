package geulsam.archive.domain.content.service;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.ContentInfoRes;
import geulsam.archive.domain.content.dto.res.ContentRes;
import geulsam.archive.domain.content.dto.res.RecentContentRes;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.contentAward.entity.ContentAward;
import geulsam.archive.domain.contentAward.repository.ContentAwardRepository;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import geulsam.archive.global.s3.DeleteManager;
import geulsam.archive.global.s3.UploadManager;
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
    private final UploadManager uploadManager;
    private final DeleteManager deleteManager;

    /**
     * Content 전체를 리턴하는 트랜잭션
     * @param genre 검색할 콘텐츠 객체의 genre
     * @param keyword 검색할 콘텐츠 객체의 제목 혹은 작가명 관련 문자열
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<ContentRes> 페이지네이션 정보와 ContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional(readOnly = true)
    public PageRes<ContentRes> getContents(Genre genre, String keyword, Pageable pageable) {
        Page<Content> contentPage;

        if (genre != null && keyword != null) {
            contentPage = contentRepository.findByGenreAndNameContainingOrUser_NameContaining(genre, keyword, keyword, pageable);
        } else if (genre != null) {
            contentPage = contentRepository.findByGenre(genre, pageable);

        } else if (keyword != null) {
            contentPage = contentRepository.findByNameContainingOrUser_NameContaining(keyword, keyword, pageable);

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

    /**
     * 최근 생성된 8개 Content만을 리턴하는 트랜잭션
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<RecentContentRes> 페이지네이션 정보와 RecentContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional
    public PageRes<RecentContentRes> getRecentContents(Pageable pageable) {
        Page<Content> recentContentPage = contentRepository.findTop8ByIsVisibleOrderByCreatedAtDesc(IsVisible.EVERY, pageable);

        List<RecentContentRes> recentContentResList = recentContentPage.getContent().stream()
                .map(RecentContentRes::new)
                .toList();

        return new PageRes<>(
                recentContentPage.getTotalPages(),
                recentContentResList
        );
    }

    /**
     * contentUploadReq 객체를 받은 뒤 객체 안의 MultipartFile을 저장하고 url을 받아 옴.
     * 받아온 url과 uploadReq 객체를 사용해 Content 객체를 만들고 repository에 저장
     * @param contentUploadReq Content 객체를 생성할 수 있는 정보와 MultipartFile이 담긴 DTO
     * @return UUID 저장한 Content 객체의 id
     */
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
                contentUploadReq.getGenre(),
                LocalDateTime.now(),
                contentUploadReq.getIsVisible(),
                contentUploadReq.getBookPage(),
                contentUploadReq.getSentence()
        );

        String pdfUrl = uploadManager.uploadFile(contentUploadReq.getPdf(), newContent.getId(), "contentPdf");
        String htmlUrl = uploadManager.uploadFile(contentUploadReq.getHtml(), newContent.getId(), "contentHtml");

        newContent.saveS3publicUrl(pdfUrl, htmlUrl);

        Content savedContent = contentRepository.save(newContent);

        return savedContent.getId();
    }

    /**
     * 특정 Content를 삭제하는 트랜잭션
     * @param field 삭제하고 싶은 문집이 가진 필드
     * @param search 삭제할 문집의 필드 값
     */
    @Transactional
    public void delete(String field, String search) {
        Content content;

        if (field.equals("id")) {
            content = contentRepository.findById(UUID.fromString(search)).orElseThrow(
                    () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 content 없음")
            );
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "유효하지 않은 검색 필드");
        }

        deleteManager.deleteFile(content.getId(), "contentPdf");
        deleteManager.deleteFile(content.getId(), "contentHtml");

        contentRepository.deleteById(content.getId());

    }
}
