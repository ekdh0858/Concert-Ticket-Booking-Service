package kr.hhplus.be.server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertCreateRequestDto {
    private String title;
    private String description;
    private String director;
}
