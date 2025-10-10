package kr.hhplus.be.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.Reservation;
import lombok.Builder;

@Builder
public record ReservationResponseDto(
    UUID reservationId,
    UUID seatId,
    UUID userId,
    LocalDateTime reservedAt,
    LocalDateTime expiresAt,
    String status
) {
    public static ReservationResponseDto fromDomain(Reservation reservation) {
        return ReservationResponseDto.builder()
            .reservationId(reservation.getId())
            .seatId(reservation.getSeatId())
            .userId(reservation.getUserId())
            .reservedAt(reservation.getReservedAt())
            .expiresAt(reservation.getExpiresAt())
            .status(reservation.getStatus().name())
            .build();
    }
}
