package kr.hhplus.be.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.dto.request.ConcertScheduleCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertScheduleResponseDto;
import kr.hhplus.be.server.entity.Concert;
import kr.hhplus.be.server.entity.ConcertSchedule;
import kr.hhplus.be.server.entity.ConcertSchedule.ScheduleStatus;
import kr.hhplus.be.server.repository.ConcertRepository;
import kr.hhplus.be.server.repository.ConcertScheduleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ConcertScheduleServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @InjectMocks
    private ConcertScheduleService concertScheduleService;

    private Concert concert;

    @BeforeEach
    void setUp() {
        concert = Concert.builder()
            .id(UUID.randomUUID())
            .title("테스트 콘서트")
            .description("테스트용 콘서트입니다.")
            .director("박태훈")
            .build();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 정리할 로직이 필요하다면 여기에 작성
    }

    @Test
    @DisplayName("createSchedule: ConcertSchedule이 정상적으로 생성되고 DTO로 반환된다")
    void createSchedule() {
        // given
        UUID concertId = concert.getId();
        ConcertScheduleCreateRequestDto dto = new ConcertScheduleCreateRequestDto(
            LocalDateTime.of(2025, 10, 15, 19, 0),
            LocalDateTime.of(2025, 10, 15, 21, 0),
            "1차 공연",
            concertId,
            ScheduleStatus.OPEN
        );

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));

        ConcertSchedule savedSchedule = ConcertSchedule.builder()
            .id(UUID.randomUUID())
            .title(dto.title())
            .startTime(dto.startTime())
            .endTime(dto.endTime())
            .status(dto.status())
            .concert(concert)
            .build();

        when(concertScheduleRepository.save(any(ConcertSchedule.class))).thenReturn(savedSchedule);

        // when
        ConcertScheduleResponseDto response = concertScheduleService.createSchedule(dto);

        // then
        verify(concertRepository, times(1)).findById(concertId);
        verify(concertScheduleRepository, times(1)).save(any(ConcertSchedule.class));

        assertThat(response.title()).isEqualTo(dto.title());
        assertThat(response.status()).isEqualTo(ScheduleStatus.OPEN);
        assertThat(response.concertId()).isEqualTo(concertId);
    }

    @Test
    @DisplayName("getAvailableConcertSchedules: OPEN 상태의 미래 스케줄만 반환된다")
    void getAvailableConcertSchedules() {
        // given
        String concertName = "테스트 콘서트";
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        ConcertSchedule schedule1 = ConcertSchedule.builder()
            .id(UUID.randomUUID())
            .title("1차 공연")
            .startTime(now.plusDays(2))
            .endTime(now.plusDays(2).plusHours(2))
            .status(ScheduleStatus.OPEN)
            .concert(concert)
            .build();

        Page<ConcertSchedule> pageResult =
            new PageImpl<>(List.of(schedule1), pageable, 1);

        when(concertScheduleRepository.findAvailableSchedules(eq(concertName), any(LocalDateTime.class), eq(pageable)))
            .thenReturn(pageResult);

        // when
        Page<ConcertScheduleResponseDto> responsePage =
            concertScheduleService.getAvailableConcertSchedules(concertName, pageable);

        // then
        verify(concertScheduleRepository, times(1))
            .findAvailableSchedules(eq(concertName), any(LocalDateTime.class), eq(pageable));

        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        ConcertScheduleResponseDto response = responsePage.getContent().get(0);
        assertThat(response.title()).isEqualTo("1차 공연");
        assertThat(response.status()).isEqualTo(ScheduleStatus.OPEN);
        assertThat(response.concertId()).isEqualTo(concert.getId());
    }
}