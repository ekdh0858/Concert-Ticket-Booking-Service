package kr.hhplus.be.server.service;

import java.util.UUID;
import kr.hhplus.be.server.dto.request.BalanceChargeRequestDto;
import kr.hhplus.be.server.dto.response.BalanceResponseDto;
import kr.hhplus.be.server.entity.UserBalance;
import kr.hhplus.be.server.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    @Transactional
    public void deduct(UUID userId, Long amount) {
        UserBalance balance = balanceRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (balance.getBalance() < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        balance.charge(-amount); // 음수 금액을 더해서 차감
        balanceRepository.save(balance);
    }

    @Transactional
    public BalanceResponseDto charge(BalanceChargeRequestDto dto) {
        UserBalance balance = balanceRepository.findById(dto.userId())
            .orElseGet(() -> UserBalance.builder()
                .userId(dto.userId())
                .balance(0L)
                .build());

        balance.charge(dto.amount());
        UserBalance saved = balanceRepository.save(balance);
        return new BalanceResponseDto(saved.getUserId(), saved.getBalance());
    }

    @Transactional(readOnly = true)
    public BalanceResponseDto getBalance(UUID userId) {
        UserBalance balance = balanceRepository.findById(userId)
            .orElseGet(() -> UserBalance.builder()
                .userId(userId)
                .balance(0L)
                .build());

        return new BalanceResponseDto(balance.getUserId(), balance.getBalance());
    }
}
