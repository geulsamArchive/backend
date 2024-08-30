package geulsam.archive.domain.book.service;

import geulsam.archive.domain.book.dto.req.UpdateReq;
import geulsam.archive.domain.book.dto.req.UploadReq;
import geulsam.archive.domain.book.dto.res.BookIdRes;
import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
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

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final UploadManager uploadManager;
    private final DeleteManager deleteManager;

    /**
     * book 전체를 리턴하는 트랜잭션
     * @return
     */
    @Transactional(readOnly = true)
    public PageRes<BookRes> book(Pageable pageable) {

        Page<Book> bookPage = bookRepository.findAll(pageable);

        List<BookRes> bookResList = bookPage.getContent().stream()
                .map(book -> new BookRes(book, bookPage.getContent().indexOf(book)))
                .collect(Collectors.toList());

        return new PageRes<>(
                bookPage.getTotalPages(),
                bookResList
        );
    }

    /**
     * Book 객체의 Id 1개를 받아 Book 의 세부사항을 리턴하는 트랜잭션
     * @param id Book 객체의 id
     * @return BookIdRes
     */
    @Transactional(readOnly = true)
    public BookIdRes bookId(String id) {

        // id 로 book 객체를 찾지 못할 시 예외 return
        Book book = bookRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ArchiveException(
                ErrorCode.VALUE_ERROR, "해당 Book 없음"
        ));

        return new BookIdRes(book);
    }

    /**
     * uploadReq 객체를 받은 뒤 객체 안의 MultipartFile 을 저장하고 url 을 받아 옴.
     * 받아온 url 과 uploadReq 객체를 사용해 Book 객체를 만들고 repository 에 저장
     * @param uploadReq Book 객체를 생성할 수 있는 정보와 MultipartFile 이 담긴 DTO
     */
    @Transactional
    public void upload(UploadReq uploadReq) {
        Book book = new Book(
                uploadReq.getDesigner(),
                uploadReq.getPlate(),
                uploadReq.getPageNumber(),
                Year.of(uploadReq.getYear()),
                uploadReq.getRelease(),
                uploadReq.getTitle()
        );

        bookRepository.save(book);

        String bookUrl = uploadManager.uploadFile(uploadReq.getPdf(), book.getId(), "book");
        String bookCoverUrl = uploadManager.uploadFile(uploadReq.getBookCover(), book.getId(), "bookCover");
        String bookCoverThumbNail = uploadManager.uploadFile(uploadReq.getBookCoverThumbnail(), book.getId(), "bookCoverThumbNail");
        String backCoverUrl = uploadManager.uploadFile(uploadReq.getBackCover(), book.getId(), "backCover");
        String backCoverThumbNail = uploadManager.uploadFile(uploadReq.getBackCoverThumbnail(), book.getId(), "backCoverThumbNail");

        book.saveS3publicUrl(bookUrl, bookCoverUrl, bookCoverThumbNail, backCoverUrl, backCoverThumbNail);
    }

    @Transactional
    public void delete(String field, String search) {
        Book book = bookRepository.findById(UUID.fromString(search)).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 book 없음")
        );

        deleteManager.deleteFile(book.getId(), "book");
        deleteManager.deleteFile(book.getId(), "bookCover");
        deleteManager.deleteFile(book.getId(), "bookCoverThumbNail");
        deleteManager.deleteFile(book.getId(), "backCover");
        deleteManager.deleteFile(book.getId(), "backCoverThumbNail");

        bookRepository.deleteById(book.getId());
    }

    @Transactional
    public void update(String search, UpdateReq updateReq) {
        Book book = bookRepository.findById(UUID.fromString(search)).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 book 없음")
        );

        String bookUrl = book.getUrl();
        String bookCoverUrl = book.getCoverUrl();
        String bookCoverThumbNail = book.getThumbNailUrl();
        String backCover = book.getBackCoverUrl();
        String backCoverThumbNail = book.getBackThumbNailUrl();

        // book URL 업데이트
        if (updateReq.getPdf().isPresent() && !updateReq.getPdf().get().isEmpty()) {
            deleteManager.deleteFile(book.getId(), "book");
            bookUrl = uploadManager.uploadFile(updateReq.getPdf().get(), book.getId(), "book");
        }

        // bookCoverURL 업데이트
        if (updateReq.getBookCover().isPresent() && !updateReq.getBookCover().get().isEmpty()) {
            deleteManager.deleteFile(book.getId(), "bookCover");
            bookCoverUrl = uploadManager.uploadFile(updateReq.getBookCover().get(), book.getId(), "bookCover");
        }

        // bookCoverThumbnail 업데이트
        if (updateReq.getBookCoverThumbnail().isPresent() && !updateReq.getBookCoverThumbnail().get().isEmpty()) {
            deleteManager.deleteFile(book.getId(), "bookCoverThumbNail");
            bookCoverThumbNail = uploadManager.uploadFile(updateReq.getBookCoverThumbnail().get(), book.getId(), "bookCoverThumbNail");
        }

        // backCover 업데이트
        if (updateReq.getBackCover().isPresent() && !updateReq.getBackCover().get().isEmpty()) {
            deleteManager.deleteFile(book.getId(), "backCover");
            backCover = uploadManager.uploadFile(updateReq.getBackCover().get(), book.getId(), "backCover");
        }

        // backCoverThumbnail 업데이트
        if (updateReq.getBackCoverThumbnail().isPresent() && !updateReq.getBackCoverThumbnail().get().isEmpty()) {
            deleteManager.deleteFile(book.getId(), "backCoverThumbNail");
            backCoverThumbNail = uploadManager.uploadFile(updateReq.getBackCoverThumbnail().get(), book.getId(), "backCoverThumbNail");
        }

        book.updateByUpdateReq(updateReq);
        book.saveS3publicUrl(bookUrl, bookCoverUrl, bookCoverThumbNail, backCover, backCoverThumbNail);
    }
}
