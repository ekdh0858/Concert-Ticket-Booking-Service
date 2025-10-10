package kr.hhplus.be.server.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;
import kr.hhplus.be.server.domain.Payment;
import kr.hhplus.be.server.domain.PaymentRepositoryPort;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import kr.hhplus.be.server.dto.response.BalanceResponseDto;
import kr.hhplus.be.server.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PayForReservationUseCaseTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @Mock
    private BalanceService balanceService;

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @InjectMocks
    private PayForReservationUseCase useCase;

    private UUID userId;
    private UUID reservationId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        reservationId = UUID.randomUUID();
    }

    @Test
    @DisplayName("잔액이 충분하면 결제가 성공하고 상태가 SUCCESS로 변경된다")
    void pay_success() {
        // given
        when(balanceService.getBalance(userId))
            .thenReturn(new BalanceResponseDto(userId, 10_000L));

        when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Payment result = useCase.pay(userId, reservationId, 5_000L);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getReservationId()).isEqualTo(reservationId);
        assertThat(result.getAmount()).isEqualTo(5_000L);
        assertThat(result.getStatus()).isEqualTo(Payment.PaymentStatus.SUCCESS);

        verify(balanceService).deduct(userId, 5_000L);
        verify(reservationRepository).markAsPaid(reservationId);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("잔액이 부족하면 IllegalStateException 발생")
    void pay_fail_when_balance_is_insufficient() {
        // given
        when(balanceService.getBalance(userId))
            .thenReturn(new BalanceResponseDto(userId, 1_000L));

        // when & then
        assertThatThrownBy(() -> useCase.pay(userId, reservationId, 5_000L))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("잔액이 부족합니다.");

        verify(balanceService, never()).deduct(any(), anyLong());
        verify(reservationRepository, never()).markAsPaid(any());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 저장 중 예외 발생 시 결제 상태는 FAILED로 변경된다")
    void pay_fails_during_processing() {
        // given
        when(balanceService.getBalance(userId))
            .thenReturn(new BalanceResponseDto(userId, 10_000L));

        doThrow(new RuntimeException("예약 업데이트 실패"))
            .when(reservationRepository).markAsPaid(reservationId);

        when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        assertThatThrownBy(() -> useCase.pay(userId, reservationId, 5_000L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("예약 업데이트 실패");

        // then
        verify(balanceService).deduct(userId, 5_000L);
        verify(reservationRepository).markAsPaid(reservationId);
        verify(paymentRepository).save(any(Payment.class));
    }
}