package kr.hhplus.be.server.application;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.Reservation;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReserveSeatUseCase {

    private final SeatRepository seatRepository;
    private final ReservationRepositoryPort reservationRepository;

    private static final int RESERVATION_HOLD_MINUTES = 5;

    @Transactional
    public Reservation reserveSeat(UUID seatId, UUID userId) {
        Seat seat = seatRepository.findByIdForUpdate(seatId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));

        // 이미 예약된 좌석이면 예외 발생
        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약 중이거나 판매 완료된 좌석입니다.");
        }

        seat.reserve();
        seatRepository.save(seat);

        // 예약 엔티티 생성
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
            .seatId(seatId)
            .userId(userId)
            .reservedAt(now)
            .expiresAt(now.plusMinutes(RESERVATION_HOLD_MINUTES))
            .status(Reservation.ReservationStatus.PENDING)
            .build();

        return reservationRepository.save(reservation);
    }
}
