package kr.hhplus.be.server.service;

import java.util.UUID;
import kr.hhplus.be.server.dto.request.ConcertCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertResponseDto;
import kr.hhplus.be.server.entity.Concert;
import kr.hhplus.be.server.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    @Transactional
    public ConcertResponseDto createConcert(ConcertCreateRequestDto dto) {
        Concert concert = Concert.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .director(dto.getDirector())
            .build();

        Concert saved = concertRepository.save(concert);
        return ConcertResponseDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ConcertResponseDto getConcertById(UUID concertId) {
        Concert concert = concertRepository.findById(concertId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘서트입니다. ID: " + concertId));

        return ConcertResponseDto.fromEntity(concert);
    }
}
