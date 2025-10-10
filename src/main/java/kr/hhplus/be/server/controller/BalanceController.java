package kr.hhplus.be.server.controller;

import java.util.UUID;
import kr.hhplus.be.server.dto.request.BalanceChargeRequestDto;
import kr.hhplus.be.server.dto.response.BalanceResponseDto;
import kr.hhplus.be.server.service.BalanceService;
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
@RequestMapping("/api/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/charge")
    public ResponseEntity<BalanceResponseDto> charge(@RequestBody BalanceChargeRequestDto dto) {
        return ResponseEntity.ok(balanceService.charge(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BalanceResponseDto> getBalance(@PathVariable UUID userId) {
        return ResponseEntity.ok(balanceService.getBalance(userId));
    }
}
