package kr.hhplus.be.server.controller;

import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.dto.response.SeatResponseDto;
import kr.hhplus.be.server.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/available")
    public ResponseEntity<List<SeatResponseDto>> getAvailableSeats(@RequestParam UUID scheduleId) {
        List<SeatResponseDto> response = seatService.getAvailableSeats(scheduleId);
        return ResponseEntity.ok(response);
    }
}
