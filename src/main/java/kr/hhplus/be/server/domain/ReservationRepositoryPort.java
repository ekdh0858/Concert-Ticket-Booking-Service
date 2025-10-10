package kr.hhplus.be.server.domain;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {
    Reservation save(Reservation reservation);
    Optional<Reservation> findBySeatId(UUID seatId);
    void markAsPaid(UUID reservationId);
}
