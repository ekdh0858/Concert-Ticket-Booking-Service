package kr.hhplus.be.server.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.dto.response.SeatResponseDto;
import kr.hhplus.be.server.entity.ConcertSchedule;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.entity.Seat.SeatStatus;
import kr.hhplus.be.server.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    private UUID scheduleId;
    private ConcertSchedule schedule;

    @BeforeEach
    void setUp() {
        scheduleId = UUID.randomUUID();
        schedule = ConcertSchedule.builder()
            .id(scheduleId)
            .title("테스트 공연")
            .startTime(LocalDateTime.now().plusDays(1))
            .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
            .status(ConcertSchedule.ScheduleStatus.OPEN)
            .build();
    }

    @Test
    @DisplayName("getAvailableSeats: 공연 일정의 AVAILABLE 상태 좌석들을 seatNumber 오름차순으로 반환한다")
    void getAvailableSeats() {
        // given
        UUID seatId1 = UUID.randomUUID();
        UUID seatId2 = UUID.randomUUID();

        Seat seat1 = Seat.builder()
            .id(seatId1)
            .seatNumber(1)
            .status(SeatStatus.AVAILABLE)
            .concertSchedule(schedule)
            .build();

        Seat seat2 = Seat.builder()
            .id(seatId2)
            .seatNumber(2)
            .status(SeatStatus.AVAILABLE)
            .concertSchedule(schedule)
            .build();

        List<Seat> mockSeats = List.of(seat1, seat2);

        when(seatRepository.findByConcertScheduleIdAndStatusOrderBySeatNumberAsc(scheduleId, SeatStatus.AVAILABLE))
            .thenReturn(mockSeats);

        // when
        List<SeatResponseDto> result = seatService.getAvailableSeats(scheduleId);

        // then
        verify(seatRepository, times(1))
            .findByConcertScheduleIdAndStatusOrderBySeatNumberAsc(scheduleId, SeatStatus.AVAILABLE);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).seatNumber()).isEqualTo(1);
        assertThat(result.get(1).seatNumber()).isEqualTo(2);
        assertThat(result.get(0).status()).isEqualTo(SeatStatus.AVAILABLE);
        assertThat(result.get(0).concertScheduleId()).isEqualTo(scheduleId);
    }
}