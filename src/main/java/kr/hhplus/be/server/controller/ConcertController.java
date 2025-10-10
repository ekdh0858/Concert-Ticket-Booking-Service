package kr.hhplus.be.server.controller;

import java.util.UUID;
import kr.hhplus.be.server.dto.request.ConcertCreateRequestDto;
import kr.hhplus.be.server.dto.response.ConcertResponseDto;
import kr.hhplus.be.server.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    public ResponseEntity<ConcertResponseDto> createConcert(@RequestBody ConcertCreateRequestDto requestDto) {
        ConcertResponseDto response = concertService.createConcert(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertResponseDto> getConcertById(@PathVariable UUID id) {
        ConcertResponseDto response = concertService.getConcertById(id);
        return ResponseEntity.ok(response);
    }
}
