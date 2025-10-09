package kr.hhplus.be.server.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;
import kr.hhplus.be.server.dto.request.ConcertScheduleCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertScheduleResponseDto;
import kr.hhplus.be.server.entity.Concert;
import kr.hhplus.be.server.entity.ConcertSchedule;
import kr.hhplus.be.server.repository.ConcertRepository;
import kr.hhplus.be.server.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertScheduleService {

    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;

    @Transactional
    public ConcertScheduleResponseDto createSchedule(ConcertScheduleCreateRequestDto dto) {
        Concert concert = concertRepository.findById(dto.concertId())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 콘서트입니다. ID: " + dto.concertId()));

        ConcertSchedule.ScheduleStatus status =
            dto.status() != null ? dto.status() : ConcertSchedule.ScheduleStatus.OPEN;

        ConcertSchedule schedule = ConcertSchedule.builder()
            .title(dto.title())
            .startTime(dto.startTime())
            .endTime(dto.endTime())
            .status(status)
            .concert(concert)
            .build();

        ConcertSchedule saved = concertScheduleRepository.save(schedule);
        return ConcertScheduleResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public Page<ConcertScheduleResponseDto> getAvailableConcertSchedules(String concertName, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<ConcertSchedule> schedules = concertScheduleRepository.findAvailableSchedules(concertName, now, pageable);
        return schedules.map(ConcertScheduleResponseDto::fromEntity);
    }
}
