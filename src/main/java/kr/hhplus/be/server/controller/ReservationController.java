package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.application.ReserveSeatUseCase;
import kr.hhplus.be.server.domain.Reservation;
import kr.hhplus.be.server.dto.request.ReservationRequestDto;
import kr.hhplus.be.server.dto.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReserveSeatUseCase reserveSeatUseCase;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> reserveSeat(@RequestBody ReservationRequestDto requestDto) {
        Reservation reservation = reserveSeatUseCase.reserveSeat(
            requestDto.seatId(),
            requestDto.userId()
        );
        return ResponseEntity.ok(ReservationResponseDto.fromDomain(reservation));
    }
}
