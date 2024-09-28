package geulsam.archive.domain.content.service;

import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import geulsam.archive.domain.content.dto.req.ContentUpdateReq;
import geulsam.archive.domain.content.dto.req.ContentUploadReq;
import geulsam.archive.domain.content.dto.res.*;
import geulsam.archive.domain.content.entity.Content;
import geulsam.archive.domain.content.entity.Genre;
import geulsam.archive.domain.content.entity.IsVisible;
import geulsam.archive.domain.content.repository.ContentRepository;
import geulsam.archive.domain.user.entity.Level;
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
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UploadManager uploadManager;
    private final DeleteManager deleteManager;

    /**
     * 조건을 만족하는 Content 를 리턴하는 트랜잭션
     * IsVisible.LOGGEDIN 타입을 가진 Content 만을 다룸.
     * @param genre 검색할 콘텐츠 객체의 genre
     * @param keyword 검색할 콘텐츠 객체의 제목 혹은 작가명 관련 문자열
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<ContentRes> 페이지네이션 정보와 ContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional(readOnly = true)
    public PageRes<ContentRes> getContentsByFiltersForLOGGEDIN(Genre genre, String keyword, Pageable pageable) {
        Page<Content> contentPage;

        if (genre != null) {
            if (keyword != null && !keyword.isEmpty()) {
                contentPage = contentRepository.findContentByFilters(IsVisible.LOGGEDIN, IsVisible.EVERY, genre, keyword, pageable);
            }
            else {
                contentPage = contentRepository.findByIsVisibleAndGenre(IsVisible.LOGGEDIN, IsVisible.EVERY, genre, pageable);
            }
        } else {
            if (keyword != null && !keyword.isEmpty()) {
                contentPage = contentRepository.findByIsVisibleAndKeyword(IsVisible.LOGGEDIN, IsVisible.EVERY, keyword, pageable);
            }
            else {
                contentPage = contentRepository.findByIsVisible(IsVisible.LOGGEDIN, IsVisible.EVERY, pageable);
            }
        }

        List<ContentRes> contentResList = contentPage.getContent().stream()
                .map(content -> new ContentRes(content, contentPage.getContent().indexOf(content)))
                .collect(Collectors.toList());

        return new PageRes<>(
                contentPage.getTotalPages(),
                contentResList
        );
    }

    /**
     * Content 전체를 리턴하는 트랜잭션
     * IsVisible.EVERY 타입을 가진 Content 만을 다룸.
     * @param genre 검색할 콘텐츠 객체의 genre
     * @param keyword 검색할 콘텐츠 객체의 제목 혹은 작가명 관련 문자열
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @return PageRes<ContentRes> 페이지네이션 정보와 ContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional(readOnly = true)
    public PageRes<ContentRes> getContentsByFiltersForANONYMOUS(Genre genre, String keyword, Pageable pageable) {
        Page<Content> contentPage;

        if (genre != null) {
            if (keyword != null && !keyword.isEmpty()) {
                contentPage = contentRepository.findContentByFilters(IsVisible.EVERY, genre, keyword, pageable);
            }
            else {
                contentPage = contentRepository.findByIsVisibleAndGenre(IsVisible.EVERY, genre, pageable);
            }
        } else {
            if (keyword != null && !keyword.isEmpty()) {
                contentPage = contentRepository.findByIsVisibleAndKeyword(IsVisible.EVERY, keyword, pageable);
            }
            else {
                contentPage = contentRepository.findByIsVisible(IsVisible.EVERY, pageable);
            }
        }

        List<ContentRes> contentResList = contentPage.getContent().stream()
                .map(content -> new ContentRes(content, contentPage.getContent().indexOf(content)))
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
     * IsVisible.EVERY과 IsVisible.LOGGEDIN 타입을 가진 Content 만을 다룸.
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @param userId 로그인한 유저의 id. 음수인 경우 InVisible.EVERY 타입을 가진 Content 만을 다루도록 한다.
     * @return PageRes<RecentContentRes> 페이지네이션 정보와 RecentContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional
    public PageRes<RecentContentRes> getContents(Pageable pageable, int userId) {
        Page<Content> recentContentPage;

        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                    ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        //User의 권한에 따른 적절한 Content 리스트 반환
        if(findUser.getLevel().equals(Level.SUSPENDED)) {
            recentContentPage = contentRepository.findByIsVisibleOrderByCreatedAtDesc(IsVisible.EVERY, pageable);
        } else if(findUser.getLevel().equals(Level.NORMAL) || findUser.getLevel().equals(Level.ADMIN)) {
            recentContentPage = contentRepository.findByIsVisibleOrderByCreatedAtDesc(IsVisible.EVERY, IsVisible.LOGGEDIN, pageable);
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "지원하지 않는 타입: " + findUser.getLevel());
        }

        List<RecentContentRes> recentContentResList = recentContentPage.getContent().stream()
                .map(RecentContentRes::new)
                .toList();

        return new PageRes<>(
                recentContentPage.getTotalPages(),
                recentContentResList
        );
    }

    /**
     * 로그인된 유저의 Content 전체를 리턴하는 트랜잭션
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @param userId 로그인한 유저의 id
     * @return PageRes<MyContentRes> 페이지네이션 정보와 MyContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional
    public PageRes<MyContentRes> getMyContents(Pageable pageable, Integer userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        Page<Content> myContentPage = contentRepository.findByUserOrderByCreatedAtDesc(findUser, pageable);

        List<MyContentRes> myContentResList = myContentPage.getContent().stream()
                .map(MyContentRes::new)
                .toList();

        return new PageRes<>(
                myContentPage.getTotalPages(),
                myContentResList
        );

    }

    /**
     * 특정 유저의 Content 리스트를 리턴하는 트랜잭션
     * 유저의 Level이 Normal이면 IsVisible.LOGGEDIN를, SUSPENDED이면 IsVisible.EVERY에 해당하는 Content 리스트를 제공함.
     * 로그인된 유저가 작가 본인일 시, IsVisible.PRIVATE에 해당하는 Content 리스트를 제공함.
     * @param pageable 페이지네이션 정보를 포함하는 Pageable 객체
     * @param authorId 조회할 유저의 id
     * @param userId 로그인한 유저의 id
     * @return PageRes<AuthorContentRes> 페이지네이션 정보와 AuthorContentRes 객체 리스트를 포함하는 PageRes 객체
     */
    @Transactional
    public PageRes<AuthorContentRes> getAuthorContent(Pageable pageable, int authorId, int userId) {
        Page<Content> authorContentPage;

        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        User findAuthor = userRepository.findById(authorId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        //User의 권한에 따른 적절한 Content 리스트 반환
        if(findUser.getLevel().equals(Level.SUSPENDED)) {
            authorContentPage = contentRepository.findByUserAndIsVisible(findAuthor, IsVisible.EVERY, pageable);
        } else if(findUser.getLevel().equals(Level.NORMAL) || findUser.getLevel().equals(Level.ADMIN)) {
            authorContentPage = contentRepository.findByUserAndIsVisible(IsVisible.EVERY, IsVisible.LOGGEDIN, findAuthor, pageable);
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "지원하지 않는 타입: " + findUser.getLevel());
        }

        List<AuthorContentRes> authorContentResList = authorContentPage.getContent().stream()
                .map(AuthorContentRes::new)
                .toList();

        return new PageRes<>(
                authorContentPage.getTotalPages(),
                authorContentResList
        );
    }

    /**
     * contentUploadReq 객체를 받은 뒤 객체 안의 MultipartFile을 저장하고 url을 받아 옴.
     * 받아온 url과 uploadReq 객체를 사용해 Content 객체를 만들고 repository에 저장
     * @param contentUploadReq Content 객체를 생성할 수 있는 정보와 MultipartFile이 담긴 DTO
     * @return UUID 저장한 Content 객체의 id
     */
    @Transactional
    public UUID upload(ContentUploadReq contentUploadReq, int userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        Book findBook = null;
        if(contentUploadReq.getBookId() != null) {
            findBook = bookRepository.findById(contentUploadReq.getBookId()).orElseThrow(() -> new ArchiveException(
                    ErrorCode.VALUE_ERROR, "해당 Book 없음"
            ));
        }

        Content newContent = new Content(
                findUser,
                findBook,
                contentUploadReq.getName(),
                contentUploadReq.getGenre(),
                LocalDateTime.now(),
                contentUploadReq.getIsVisible(),
                contentUploadReq.getBookPage(),
                contentUploadReq.getSentence()
        );

        Content savedContent = contentRepository.save(newContent);

        String pdfUrl = (contentUploadReq.getPdf().isEmpty()) ? null : uploadManager.uploadFile(contentUploadReq.getPdf(), newContent.getId(), "contentPdf");
        String htmlUrl = (contentUploadReq.getHtml().isEmpty()) ? null : uploadManager.uploadFile(contentUploadReq.getHtml(), newContent.getId(), "contentHtml");

        newContent.saveS3publicUrl(pdfUrl, htmlUrl);

        return savedContent.getId();
    }

    /**
     * 특정 Content를 삭제하는 트랜잭션
     * @param contentId 삭제하고 싶은 Content 객체의 id
     */
    @Transactional
    public void delete(String contentId, int userId) {

        Content findContent = contentRepository.findById(UUID.fromString(contentId)).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 content 없음")
        );

        if(!findContent.getUser().getId().equals(userId)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "사용자 권한 없음");
        }

        deleteManager.deleteFile(findContent.getId(), "contentPdf");
        deleteManager.deleteFile(findContent.getId(), "contentHtml");

        contentRepository.deleteById(findContent.getId());
    }

    /**
     * 특정 Content를 수정하는 트랜잭션
     * @param contentId 수정하고 싶은 Content 객체의 id
     * @param contentUpdateReq Content 객체 수정에 필요한 정보가 담긴 DTO
     * @param userId 로그인한 유저의 id
     * @return ContentInfoRes 수정한 Content 객체의 정보를 포함한 DTO
     */
    public ContentInfoRes update(String contentId, ContentUpdateReq contentUpdateReq, Integer userId) {

        Content findContent = contentRepository.findById(UUID.fromString(contentId)).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        if(!findContent.getUser().getId().equals(userId)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "사용자 권한 없음");
        }

        if(Objects.nonNull(contentUpdateReq.getBookId())) {
            Book findBook = bookRepository.findById(contentUpdateReq.getBookId()).orElseThrow(() -> new ArchiveException(
                    ErrorCode.VALUE_ERROR, "해당 Book 없음"
            ));
            findContent.changeBook(findBook);
        }

        if(Objects.nonNull(contentUpdateReq.getName()) && !contentUpdateReq.getName().isEmpty()) {
            findContent.changeName(contentUpdateReq.getName());
        }

        if(Objects.nonNull(contentUpdateReq.getPdf()) && !contentUpdateReq.getPdf().isEmpty()) {
            deleteManager.deleteFile(findContent.getId(), "contentPdf");
            String pdfUrl = uploadManager.uploadFile(contentUpdateReq.getPdf(), findContent.getId(), "contentPdf");

            findContent.changePdfUrl(pdfUrl);
        }

        if(Objects.nonNull(contentUpdateReq.getHtml()) && !contentUpdateReq.getHtml().isEmpty()) {
            deleteManager.deleteFile(findContent.getId(), "contentHtml");
            String htmlUrl = uploadManager.uploadFile(contentUpdateReq.getHtml(), findContent.getId(), "contentHtml");

            findContent.changeHtmlUrl(htmlUrl);
        }

        if(Objects.nonNull(contentUpdateReq.getGenre())) {
            findContent.changeGenre(contentUpdateReq.getGenre());
        }

        if(Objects.nonNull(contentUpdateReq.getIsVisible())) {
            findContent.changeIsVisible(contentUpdateReq.getIsVisible());
        }

        if(Objects.nonNull(contentUpdateReq.getBookPage())) {
            findContent.changeBookPage(contentUpdateReq.getBookPage());
        }

        if(Objects.nonNull(contentUpdateReq.getSentence()) && !contentUpdateReq.getSentence().isEmpty()) {
            findContent.changeSentence(contentUpdateReq.getSentence());
        }

        Content savedContent = contentRepository.save(findContent);

        return new ContentInfoRes(savedContent);
    }

    /**
     * 특정 Content 에 Award 를 부여하는 트랜잭션
     * LEVEL.ADMIN 타입을 가진 User 만이 실행 가능하다.
     * @param contentId 수상 받을 Content 객체의 고유 ID
     * @param award 수상될 상의 이름을 담은 문자열
     * @param userId 로그인한 유저의 id
     * @return String 수상된 Award 의 이름
     */
    @Transactional
    public String presentAward(String contentId, String award, Integer userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 User 없음"
        ));

        if(!findUser.getLevel().equals(Level.ADMIN)) {
            throw new ArchiveException(ErrorCode.AUTHORITY_ERROR, "사용자 권한 없음");
        }

        Content findContent = contentRepository.findById(UUID.fromString(contentId)).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Content 없음"
        ));

        findContent.presentAward(award);

        Content savedContent = contentRepository.save(findContent);

        return savedContent.getAward();
    }
}
