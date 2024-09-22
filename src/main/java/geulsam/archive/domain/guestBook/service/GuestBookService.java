package geulsam.archive.domain.guestBook.service;

import geulsam.archive.domain.criticismLog.dto.res.CriticismLogRes;
import geulsam.archive.domain.guestBook.dto.req.UploadReq;
import geulsam.archive.domain.guestBook.dto.res.GuestBookRes;
import geulsam.archive.domain.guestBook.entity.GuestBook;
import geulsam.archive.domain.guestBook.repository.GuestBookRepository;
import geulsam.archive.domain.user.entity.User;
import geulsam.archive.domain.user.repository.UserRepository;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final UserRepository userRepository;

    /**
     * 방명록 등록
     * @param uploadReq ownerId, writerId, writing
     */
    @Transactional
    public void upload(UploadReq uploadReq, int writerId) {
        User owner = userRepository.findById(uploadReq.getOwnerId()).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "방명록 작성 대상자 없음")
        );

        User writer = userRepository.findById(writerId).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "방명록 작성자 없음")
        );

        guestBookRepository.save(new GuestBook(writer,owner, uploadReq.getWriting()));
    }

    /**
     * 방명록 삭제
     * @param id 방명록 테이블의 id (Integer)
     */
    @Transactional
    public void delete(int id, String role, int userId) {
        GuestBook guestBook = guestBookRepository.findById(id).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, id + "지울 방명록 객체 없음")
        );

        if(guestBook.getWriter().getId().equals(userId) || guestBook.getOwner().getId().equals(userId) || role.equals("ROLE_ADMIN")){
            guestBookRepository.deleteById(id);
        } else {
            throw new ArchiveException(ErrorCode.VALUE_ERROR, "지울 수 없음. 방명록 작성자 혹은 소유자이거나 ADMIN이어야 가능합니다.");
        }
    }

    /**
     * ownerId 를 가진 방명록 객체를 리턴
     * @param pageable
     * @param ownerId
     * @return
     */
    @Transactional
    public PageRes<GuestBookRes> guestBook(Pageable pageable, int ownerId) {
        Page<GuestBook> guestBookPage = guestBookRepository.findGuestBooksByOwnerId(pageable, ownerId);

        AtomicInteger index = new AtomicInteger(0);
        List<GuestBookRes> guestBookPageRes = guestBookPage.getContent().stream()
                .map(guestBook -> new GuestBookRes(guestBook, index.getAndIncrement()))
                .toList();

        return new PageRes<>(
                guestBookPage.getTotalPages(),
                guestBookPageRes
        );
    }
}
