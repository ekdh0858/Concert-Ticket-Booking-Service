package kr.hhplus.be.server.dto.response;

import java.util.UUID;
import kr.hhplus.be.server.entity.Concert;
import lombok.Builder;

@Builder
public record ConcertResponseDto(UUID id, String title, String description, String director) {

    public static ConcertResponseDto fromEntity(Concert concert) {
        return ConcertResponseDto.builder()
            .id(concert.getId())
            .title(concert.getTitle())
            .description(concert.getDescription())
            .director(concert.getDirector())
            .build();
    }
}
