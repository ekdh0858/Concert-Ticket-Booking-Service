package kr.hhplus.be.server.repository;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.entity.ConcertSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertScheduleRepository extends JpaRepository<ConcertSchedule, UUID> {

    @Query("""
        SELECT s FROM ConcertSchedule s
        WHERE s.status = 'OPEN'
          AND s.startTime > :now
          AND (:concertName IS NULL OR s.concert.title LIKE %:concertName%)
        ORDER BY s.startTime ASC
        """)
    Page<ConcertSchedule> findAvailableSchedules(
        @Param("concertName") String concertName,
        @Param("now") LocalDateTime now,
        Pageable pageable);
}
