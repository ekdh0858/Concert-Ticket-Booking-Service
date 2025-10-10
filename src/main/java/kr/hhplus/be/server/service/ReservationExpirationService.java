package kr.hhplus.be.server.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.Reservation;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationExpirationService {

    private final ReservationRepositoryPort reservationRepository;
    private final SeatRepository seatRepository;

    // 1분마다 실행
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();

        List<Reservation> expiredReservations = reservationRepository.findAllExpired(now);

        for (Reservation reservation : expiredReservations) {
            Seat seat = seatRepository.findById(reservation.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("좌석 없음"));

            // 상태를 AVAILABLE로 되돌림
            seat.cancelReservation();
            seatRepository.save(seat);

            // 예약 상태도 CANCELLED로 변경
            reservation.cancel();
            reservationRepository.save(reservation);
        }
    }
}
