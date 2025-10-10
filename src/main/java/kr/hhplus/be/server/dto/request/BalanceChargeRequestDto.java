package kr.hhplus.be.server.dto.request;

import java.util.UUID;

public record BalanceChargeRequestDto(
    UUID userId,
    Long amount
) {}
