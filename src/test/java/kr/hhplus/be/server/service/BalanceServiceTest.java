package kr.hhplus.be.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import kr.hhplus.be.server.dto.request.BalanceChargeRequestDto;
import kr.hhplus.be.server.dto.response.BalanceResponseDto;
import kr.hhplus.be.server.entity.UserBalance;
import kr.hhplus.be.server.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    void charge_new_user_creates_and_updates_balance() {
        // given
        BalanceChargeRequestDto dto = new BalanceChargeRequestDto(userId, 1000L);
        when(balanceRepository.findById(userId)).thenReturn(Optional.empty());
        when(balanceRepository.save(any(UserBalance.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        BalanceResponseDto response = balanceService.charge(dto);

        // then
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.balance()).isEqualTo(1000L);
        verify(balanceRepository).save(any(UserBalance.class));
    }

    @Test
    void charge_existing_user_adds_balance() {
        // given
        UserBalance existing = UserBalance.builder()
            .userId(userId)
            .balance(1000L)
            .build();

        BalanceChargeRequestDto dto = new BalanceChargeRequestDto(userId, 500L);
        when(balanceRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(balanceRepository.save(any(UserBalance.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        BalanceResponseDto response = balanceService.charge(dto);

        // then
        assertThat(response.balance()).isEqualTo(1500L);
        verify(balanceRepository).save(existing);
    }

    @Test
    void getBalance_returns_zero_when_not_found() {
        when(balanceRepository.findById(userId)).thenReturn(Optional.empty());

        BalanceResponseDto response = balanceService.getBalance(userId);

        assertThat(response.balance()).isEqualTo(0L);
    }
}