package kr.hhplus.be.server.infrastructure.reservation;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.domain.Reservation;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryPort {

    private final ReservationJpaRepository jpaRepository;

    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        ReservationEntity entity = ReservationMapper.toEntity(reservation);
        jpaRepository.save(entity);
        return ReservationMapper.toDomain(entity);
    }

    @Override
    public Optional<Reservation> findBySeatId(UUID seatId) {
        return jpaRepository.findBySeatId(seatId)
            .map(ReservationMapper::toDomain);
    }

    @Override
    public void markAsPaid(UUID reservationId) {
        ReservationEntity reservation = jpaRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        reservation.updateStatus(ReservationEntity.ReservationStatus.CONFIRMED);
    }
}
