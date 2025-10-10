package kr.hhplus.be.server.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Payment {
    private UUID id;
    private UUID userId;
    private UUID reservationId;
    private Long amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED
    }

    public static Payment create(UUID userId, UUID reservationId, Long amount) {
        return Payment.builder()
            .userId(userId)
            .reservationId(reservationId)
            .amount(amount)
            .status(PaymentStatus.PENDING)
            .paidAt(LocalDateTime.now())
            .build();
    }

    public Payment complete() {
        this.status = PaymentStatus.SUCCESS;
        return this;
    }

    public Payment fail() {
        this.status = PaymentStatus.FAILED;
        return this;
    }
}
