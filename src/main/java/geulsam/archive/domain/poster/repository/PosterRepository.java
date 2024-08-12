package geulsam.archive.domain.poster.repository;

import geulsam.archive.domain.poster.entity.Poster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PosterRepository extends JpaRepository<Poster, UUID> {

    // 전체를 검색하는 함수
    @Override
    Page<Poster> findAll(Pageable pageable);
}
