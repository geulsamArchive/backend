package geulsam.archive.domain.book.service;

import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.entity.Book;
import geulsam.archive.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                            null,
                            book.getYear().getValue(),
                            null, // 현재 설명은 null 로, 추가될 수도, 추가 안 될 수도 있음
                            book.getId().toString(),
                            book.getCreatedAt()
                    );
                }).collect(Collectors.toList());
    }
}
