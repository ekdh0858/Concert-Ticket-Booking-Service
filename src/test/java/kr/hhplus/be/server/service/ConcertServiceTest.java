package kr.hhplus.be.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.dto.request.ConcertCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertResponseDto;
import kr.hhplus.be.server.entity.Concert;
import kr.hhplus.be.server.repository.ConcertRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("createConcert: 콘서트를 생성하면 Repository.save가 호출되고 DTO가 반환된다")
    void createConcert() {
        // given
        ConcertCreateRequestDto dto = new ConcertCreateRequestDto("테스트 콘서트", "테스트 설명", "박태훈");

        Concert mockConcert = Concert.builder()
            .id(UUID.randomUUID())
            .title(dto.getTitle())
            .description(dto.getDescription())
            .director(dto.getDirector())
            .build();

        when(concertRepository.save(any(Concert.class))).thenReturn(mockConcert);

        // when
        ConcertResponseDto response = concertService.createConcert(dto);

        // then
        verify(concertRepository, times(1)).save(any(Concert.class));
        assertThat(response.title()).isEqualTo(dto.getTitle());
        assertThat(response.description()).isEqualTo(dto.getDescription());
        assertThat(response.director()).isEqualTo(dto.getDirector());
        assertThat(response.id()).isNotNull();
    }

    @Test
    @DisplayName("getConcertById: 존재하는 ID로 조회하면 DTO가 반환된다")
    void getConcertById() {
        // given
        UUID id = UUID.randomUUID();
        Concert concert = Concert.builder()
            .id(id)
            .title("조회 테스트 콘서트")
            .description("설명입니다")
            .director("박태훈")
            .build();

        when(concertRepository.findById(id)).thenReturn(Optional.of(concert));

        // when
        ConcertResponseDto response = concertService.getConcertById(id);

        // then
        verify(concertRepository, times(1)).findById(id);
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.title()).isEqualTo("조회 테스트 콘서트");
        assertThat(response.director()).isEqualTo("박태훈");
    }

    @Test
    @DisplayName("getConcertById: 존재하지 않는 ID 조회 시 예외가 발생한다")
    void getConcertById_NotFound() {
        // given
        UUID invalidId = UUID.randomUUID();
        when(concertRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            concertService.getConcertById(invalidId);
        });
        verify(concertRepository, times(1)).findById(invalidId);
    }
}