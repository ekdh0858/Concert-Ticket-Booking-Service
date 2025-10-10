package kr.hhplus.be.server.repository;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.entity.Seat.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    // 특정 공연 일정의 예약 가능한 좌석만 조회
    List<Seat> findByConcertScheduleIdAndStatusOrderBySeatNumberAsc(UUID concertScheduleId, SeatStatus status);
}

