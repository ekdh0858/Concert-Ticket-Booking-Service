package kr.hhplus.be.server.dto.request;

import lombok.Builder;

@Builder
public record ConcertCreateRequestDto(String title, String description, String director) {

}
