package geulsam.archive.domain.book.controller;

import geulsam.archive.domain.book.dto.res.BookIdRes;
import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.service.BookService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/book")
public class BookController {

    private final BookService bookService;

    /**
     * DB 에 있는 book 컬럼을 paging 해서 return
     * @param page 원하는 페이지 번호
     * @return PageRes<BookRes>
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<PageRes<BookRes>>> book(
            @RequestParam(defaultValue = "1") int page
    ){
        Pageable pageable = PageRequest.of(page - 1, 12, Sort.by(Sort.Order.asc("year")));

        PageRes<BookRes> book = bookService.book(pageable);

        return ResponseEntity.ok().body(
                SuccessResponse.<PageRes<BookRes>>builder()
                        .data(book)
                        .message("books get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

    /**
     * BookId Controller
     * @param id Book 객체의 id
     * @return BookIdRes
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<BookIdRes>> bookId(@PathVariable String id){
        BookIdRes bookIdRes = bookService.bookId(id);

        return ResponseEntity.ok().body(
                SuccessResponse.<BookIdRes>builder()
                        .data(bookIdRes)
                        .message("information of the book get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
