package kr.hhplus.be.server.dto.request;

import java.util.UUID;

public record PaymentRequestDto(UUID userId, UUID reservationId, Long amount) {}
