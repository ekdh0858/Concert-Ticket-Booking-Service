package kr.hhplus.be.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import kr.hhplus.be.server.entity.ConcertSchedule;
import kr.hhplus.be.server.entity.ConcertSchedule.ScheduleStatus;
import lombok.Builder;

@Builder
public record ConcertScheduleResponseDto(
    UUID id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String title,
    ScheduleStatus status,
    UUID concertId
) {

    public static ConcertScheduleResponseDto fromEntity(ConcertSchedule schedule) {
        return ConcertScheduleResponseDto.builder()
            .id(schedule.getId())
            .startTime(schedule.getStartTime())
            .endTime(schedule.getEndTime())
            .title(schedule.getTitle())
            .status(schedule.getStatus())
            .concertId(schedule.getConcert().getId())
            .build();
    }
}
