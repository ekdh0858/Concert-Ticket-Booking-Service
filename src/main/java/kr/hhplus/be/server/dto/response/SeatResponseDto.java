package kr.hhplus.be.server.dto.response;

import java.util.UUID;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.entity.Seat.SeatStatus;
import lombok.Builder;

@Builder
public record SeatResponseDto(
    UUID id,
    int seatNumber,
    SeatStatus status,
    UUID concertScheduleId
) {

    public static SeatResponseDto fromEntity(Seat seat) {
        return SeatResponseDto.builder()
            .id(seat.getId())
            .seatNumber(seat.getSeatNumber())
            .status(seat.getStatus())
            .concertScheduleId(seat.getConcertSchedule().getId())
            .build();
    }
}
