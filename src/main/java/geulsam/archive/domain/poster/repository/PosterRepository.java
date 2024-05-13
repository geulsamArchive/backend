package geulsam.archive.domain.poster.repository;

import geulsam.archive.domain.poster.entity.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PosterRepository extends JpaRepository<Poster, UUID> {

    // 전체를 검색하는 함수
    @Override
    List<Poster> findAll();
}
