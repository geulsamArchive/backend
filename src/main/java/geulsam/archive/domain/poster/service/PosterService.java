package geulsam.archive.domain.poster.service;

import geulsam.archive.domain.poster.dto.req.UploadReq;
import geulsam.archive.global.common.dto.PageRes;
import geulsam.archive.domain.poster.dto.res.PosterRes;
import geulsam.archive.domain.poster.entity.Poster;
import geulsam.archive.domain.poster.repository.PosterRepository;
import geulsam.archive.global.exception.ArchiveException;
import geulsam.archive.global.exception.ErrorCode;
import geulsam.archive.global.s3.DeleteManager;
import geulsam.archive.global.s3.PreSignedUrlManager;
import geulsam.archive.global.s3.UploadManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PosterService {

    private final PosterRepository posterRepository;
    private final UploadManager uploadManager;
    private final DeleteManager deleteManager;

    /**
     * Poster 전체를 검색해서 PosterRes 배열로 반환
     * @return List
     */
    @Transactional(readOnly = true)
    public PageRes<PosterRes> poster(Pageable pageable) {
        Page<Poster> posterPage = posterRepository.findAll(pageable);

        //Poster 객체를 PosterRes 객체로 mapping
        List<PosterRes> posterResList = posterPage.getContent().stream()
                .map(poster -> new PosterRes(poster, posterPage.getContent().indexOf(poster)))
                .collect(Collectors.toList());

        return new PageRes<>(
                posterPage.getTotalPages(),
                posterResList
        );
    }

    /**
     * uploadReq 객체를 받은 뒤 객체 안의 MultipartFile 을 저장하고 url 을 받아 옴.
     * 받아온 url 과 uploadReq 객체를 사용해 Poster 객체를 만들고 repository 에 저장
     * @param uploadReq Poster 객체를 생성할 수 있는 정보와 MultipartFile 이 담긴 DTO
     */

    /**
     *
     * @param uploadReq
     */
    @Transactional
    public void upload(UploadReq uploadReq) {
        Poster poster = new Poster(
                Year.of(uploadReq.getYear()),
                uploadReq.getDesigner(),
                uploadReq.getPlate()
        );

        posterRepository.save(poster);

        String posterUrl = uploadManager.uploadFile(uploadReq.getImage(), poster.getId(), "poster");
        String posterThumbNailUrl = uploadManager.uploadFile(uploadReq.getThumbNail(), poster.getId(), "posterThumbNail");

        poster.saveS3publicUrl(posterUrl, posterThumbNailUrl);
    }

    @Transactional
    public void delete(String field, String search) {
        Poster poster = posterRepository.findById(UUID.fromString(search)).orElseThrow(
                () -> new ArchiveException(ErrorCode.VALUE_ERROR, "해당 id의 poster 없음")
        );

        deleteManager.deleteFile(poster.getId(), "poster");
        deleteManager.deleteFile(poster.getId(), "posterThumbNail");

        posterRepository.deleteById(poster.getId());
    }
}
