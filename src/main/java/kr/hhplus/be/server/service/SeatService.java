package kr.hhplus.be.server.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.hhplus.be.server.dto.response.SeatResponseDto;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.entity.Seat.SeatStatus;
import kr.hhplus.be.server.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<SeatResponseDto> getAvailableSeats(UUID concertScheduleId) {
        List<Seat> seats = seatRepository.findByConcertScheduleIdAndStatusOrderBySeatNumberAsc(concertScheduleId,
            SeatStatus.AVAILABLE);
        return seats.stream()
            .map(SeatResponseDto::fromEntity)
            .collect(Collectors.toList());
    }
}
