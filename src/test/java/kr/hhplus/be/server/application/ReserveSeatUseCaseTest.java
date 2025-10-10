package kr.hhplus.be.server.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.domain.Reservation;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import kr.hhplus.be.server.entity.ConcertSchedule;
import kr.hhplus.be.server.entity.Seat;
import kr.hhplus.be.server.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReserveSeatUseCaseTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ReservationRepositoryPort reservationRepository;

    @InjectMocks
    private ReserveSeatUseCase reserveSeatUseCase;

    private ConcertSchedule concertSchedule;

    @BeforeEach
    void setUp() {
        concertSchedule = ConcertSchedule.builder()
            .id(UUID.randomUUID())
            .title("테스트 공연 일정")
            .startTime(LocalDateTime.now().plusDays(1))
            .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
            .status(ConcertSchedule.ScheduleStatus.OPEN)
            .build();
    }

    @Test
    @DisplayName("좌석이 AVAILABLE 상태일 때 예약을 생성하고 5분간 점유한다")
    void reserveSeat_success() {
        // given
        UUID seatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Seat seat = Seat.builder()
            .id(seatId)
            .seatNumber(1)
            .status(Seat.SeatStatus.AVAILABLE)
            .concertSchedule(concertSchedule)
            .build();

        when(seatRepository.findByIdForUpdate(seatId)).thenReturn(Optional.of(seat));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);
        when(reservationRepository.save(any(Reservation.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Reservation result = reserveSeatUseCase.reserveSeat(seatId, userId);

        // then
        assertThat(result.getSeatId()).isEqualTo(seatId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getStatus()).isEqualTo(Reservation.ReservationStatus.PENDING);
        assertThat(result.getExpiresAt()).isAfter(result.getReservedAt());
        assertThat(result.getExpiresAt()).isBefore(result.getReservedAt().plusMinutes(6)); // 5분 내

        verify(seatRepository).save(any(Seat.class));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("이미 RESERVED 상태 좌석은 예약할 수 없다")
    void reserveSeat_alreadyReserved() {
        // given
        UUID seatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Seat seat = Seat.builder()
            .id(seatId)
            .seatNumber(1)
            .status(Seat.SeatStatus.RESERVED)
            .concertSchedule(concertSchedule)
            .build();

        when(seatRepository.findByIdForUpdate(seatId)).thenReturn(Optional.of(seat));

        // when & then
        assertThatThrownBy(() -> reserveSeatUseCase.reserveSeat(seatId, userId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 예약 중이거나 판매 완료된 좌석");

        verify(seatRepository).findByIdForUpdate(seatId);
    }

    @Test
    @DisplayName("존재하지 않는 좌석이면 예외가 발생한다")
    void reserveSeat_seatNotFound() {
        // given
        UUID seatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(seatRepository.findByIdForUpdate(seatId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reserveSeatUseCase.reserveSeat(seatId, userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("존재하지 않는 좌석");

        verify(seatRepository).findByIdForUpdate(seatId);
    }
}