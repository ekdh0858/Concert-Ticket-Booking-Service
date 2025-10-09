package kr.hhplus.be.server.dto.request;

public record ConcertCreateRequestDto(
    String title,
    String description,
    String director) {
}
