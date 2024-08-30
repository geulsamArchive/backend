package geulsam.archive.domain.criticismLog.repository;

import geulsam.archive.domain.criticismLog.entity.CriticismLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CriticismLogRepository extends JpaRepository<CriticismLog,UUID>{
}
