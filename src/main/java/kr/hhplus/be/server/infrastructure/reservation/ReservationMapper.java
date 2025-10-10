package kr.hhplus.be.server.infrastructure.reservation;

import kr.hhplus.be.server.domain.Reservation;

public class ReservationMapper {

    public static ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.builder()
            .id(reservation.getId())
            .seatId(reservation.getSeatId())
            .userId(reservation.getUserId())
            .reservedAt(reservation.getReservedAt())
            .expiresAt(reservation.getExpiresAt())
            .status(toEntityStatus(reservation.getStatus()))
            .build();
    }

    public static Reservation toDomain(ReservationEntity entity) {
        return Reservation.builder()
            .id(entity.getId())
            .seatId(entity.getSeatId())
            .userId(entity.getUserId())
            .reservedAt(entity.getReservedAt())
            .expiresAt(entity.getExpiresAt())
            .status(toDomainStatus(entity.getStatus()))
            .build();
    }

    static ReservationEntity.ReservationStatus toEntityStatus(
        Reservation.ReservationStatus status) {
        return ReservationEntity.ReservationStatus.valueOf(status.name());
    }

    static Reservation.ReservationStatus toDomainStatus(
        ReservationEntity.ReservationStatus status) {
        return Reservation.ReservationStatus.valueOf(status.name());
    }
}
