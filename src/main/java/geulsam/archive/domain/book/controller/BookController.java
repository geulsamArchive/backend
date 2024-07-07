package geulsam.archive.domain.book.controller;

import geulsam.archive.domain.book.dto.req.UploadReq;
import geulsam.archive.domain.book.dto.res.BookIdRes;
import geulsam.archive.domain.book.dto.res.BookRes;
import geulsam.archive.domain.book.service.BookService;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.common.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Book upload 메소드
     * @param uploadReq Book 객체 생성에 필요한 정보를 담은 DTO
     * @return null
     */
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "문집 등록 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> upload(@ModelAttribute UploadReq uploadReq){

        bookService.upload(uploadReq);

        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("문집 업로드 성공")
                        .build()
        );
    }

    /**
     * Book delete 메소드
     * @param field 기본값은 id, 삭제하고 싶은 문집이 가진 필드를 지정(ex. id, designer, plate...)
     * @param search 기본값 없음. 삭제할 문집의 필드의 값을 지정
     * @return
     */
    @DeleteMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "문집 삭제 성공",
                    useReturnTypeSchema = true
            )
    })
    public ResponseEntity<SuccessResponse<Void>> delete(
            @RequestParam(defaultValue = "id") String field,
            @RequestParam String search
    ){
        bookService.delete(field, search);
        return ResponseEntity.ok().body(
                SuccessResponse.<Void>builder()
                        .data(null)
                        .status(HttpStatus.OK.value())
                        .message("문집 삭제 성공")
                        .build()
        );
    }
}
