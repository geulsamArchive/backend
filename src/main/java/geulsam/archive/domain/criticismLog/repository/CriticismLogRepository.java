package geulsam.archive.domain.criticismLog.repository;

import geulsam.archive.domain.criticismLog.entity.CriticismLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface CriticismLogRepository extends JpaRepository<CriticismLog,UUID>{

    /**
     * 이미 같은 발표자 이름, 작품 제목, 합평 날짜를 가진 객체가 있는지 카운트하는 함수
     * @param userName 발표자 이름
     * @param contentTitle 컨텐츠 제목
     * @param localDate 날짜
     * @return 위의 조건이 모두 일치하는 객체가 있다면 TRUE, 없으면 FALSE
     */
    @Query("SELECT COUNT(cl) > 0 FROM CriticismLog cl WHERE cl.userName = :userName AND cl.contentTitle = :contentTitle AND cl.localDate = :localDate")
    boolean existsCriticismLogBy(@Param("userName") String userName,
                                 @Param("contentTitle") String contentTitle,
                                 @Param("localDate")LocalDate localDate);


    @Query("SELECT cl FROM CriticismLog cl " +
            "WHERE LOWER(cl.userName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(cl.contentTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CriticismLog> findCriticismLogByFilters(
            Pageable pageable,
            @Param("keyword") String keyword
    );
}
