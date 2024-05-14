package geulsam.archive.domain.book.service;

import geulsam.archive.domain.book.dto.res.BookIdRes;
import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * book 전체를 리턴하는 트랜잭션
     * @return
     */
    @Transactional(readOnly = true)
    public List<BookRes> book() {

        List<Book> bookList = bookRepository.findAll();

        return IntStream.range(0, bookList.size())
                .mapToObj(i -> {
                    Book book = bookList.get(i);
                    return new BookRes(
                            i,
                            book.getCoverUrl(),
                            book.getYear().getValue(),
                            null, // 현재 설명은 null 로, 추가될 수도, 추가 안 될 수도 있음:wq
                            book.getId().toString(),
                            book.getCreatedAt()
                    );
                }).collect(Collectors.toList());
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

        return new BookIdRes(
                book.getId().toString(),
                book.getRelease(),
                book.getDesigner(),
                book.getPlate(),
                book.getPageNumber(),
                book.getUrl()
        );
    }
}
