package kr.hhplus.be.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.domain.Payment;

public record PaymentResponseDto(UUID id, UUID userId, UUID reservationId, Long amount, String status, LocalDateTime paidAt) {
    public static PaymentResponseDto from(Payment payment) {
        return new PaymentResponseDto(
            payment.getId(),
            payment.getUserId(),
            payment.getReservationId(),
            payment.getAmount(),
            payment.getStatus().name(),
            payment.getPaidAt()
        );
    }
}
