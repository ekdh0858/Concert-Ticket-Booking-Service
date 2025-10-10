package kr.hhplus.be.server.dto.request;

import java.util.UUID;

public record ReservationRequestDto(UUID seatId, UUID userId) {

}
