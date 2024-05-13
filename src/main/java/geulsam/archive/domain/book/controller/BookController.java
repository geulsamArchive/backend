package geulsam.archive.domain.book.controller;

import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.service.BookService;
import geulsam.archive.global.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/book")
public class BookController {

    private final BookService bookService;

    /**Book Controller
     * DB에 있는 모든 문집의 정보를 Return
     */
    @GetMapping()
    public ResponseEntity<SuccessResponse<List<BookRes>>> book(){

        List<BookRes> book = bookService.book();

        return ResponseEntity.ok().body(
                SuccessResponse.<List<BookRes>>builder()
                        .data(book)
                        .message("books get success")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
