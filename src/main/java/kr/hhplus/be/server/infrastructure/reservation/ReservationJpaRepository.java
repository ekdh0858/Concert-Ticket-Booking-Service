package kr.hhplus.be.server.infrastructure.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {

    // 특정 좌석에 대한 예약이 존재하는지 조회 (좌석 점유 여부 확인용)
    Optional<ReservationEntity> findBySeatId(UUID seatId);

    @Query("SELECT r FROM ReservationEntity r WHERE r.status = 'PENDING' AND r.expiresAt < :now")
    List<ReservationEntity> findAllExpired(@Param("now") LocalDateTime now);
}
