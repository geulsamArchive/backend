package geulsam.archive.domain.book.service;

import geulsam.archive.domain.book.dto.req.UploadReq;
import geulsam.archive.domain.book.dto.res.BookIdRes;
import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
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
     * @return
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

        book.saveS3publicUrl(bookUrl, bookCoverUrl);
    }
}
