package kr.hhplus.be.server.dto.response;

import java.util.UUID;

public record BalanceResponseDto(
    UUID userId,
    Long balance
) {}
