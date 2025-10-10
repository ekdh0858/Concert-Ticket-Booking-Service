package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.application.PayForReservationUseCase;
import kr.hhplus.be.server.domain.Payment;
import kr.hhplus.be.server.dto.request.PaymentRequestDto;
import kr.hhplus.be.server.dto.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PayForReservationUseCase payForReservationUseCase;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> pay(@RequestBody PaymentRequestDto dto) {
        Payment payment = payForReservationUseCase.pay(dto.userId(), dto.reservationId(), dto.amount());
        return ResponseEntity.ok(PaymentResponseDto.from(payment));
    }
}
