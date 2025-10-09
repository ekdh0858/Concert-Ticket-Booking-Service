package kr.hhplus.be.server.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.entity.ConcertSchedule.ScheduleStatus;

/**
 * @param concertId 어떤 콘서트의 일정인지
 * @param status    OPEN, CLOSED, CANCELLED
 */
public record ConcertScheduleCreateRequestDto(
    LocalDateTime startTime,
    LocalDateTime endTime,
    String title,
    UUID concertId,
    ScheduleStatus status) {

}
