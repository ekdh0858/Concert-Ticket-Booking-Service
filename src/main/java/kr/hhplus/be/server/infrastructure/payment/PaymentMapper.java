package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.Payment;

public class PaymentMapper {
    public static PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
            .id(payment.getId())
            .userId(payment.getUserId())
            .reservationId(payment.getReservationId())
            .amount(payment.getAmount())
            .status(PaymentEntity.PaymentStatus.valueOf(payment.getStatus().name()))
            .paidAt(payment.getPaidAt())
            .build();
    }

    public static Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .reservationId(entity.getReservationId())
            .amount(entity.getAmount())
            .status(Payment.PaymentStatus.valueOf(entity.getStatus().name()))
            .paidAt(entity.getPaidAt())
            .build();
    }
}
