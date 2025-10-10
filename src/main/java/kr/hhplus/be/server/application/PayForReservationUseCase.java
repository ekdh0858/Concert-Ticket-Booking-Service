package kr.hhplus.be.server.application;

import java.util.UUID;
import kr.hhplus.be.server.domain.Payment;
import kr.hhplus.be.server.domain.PaymentRepositoryPort;
import kr.hhplus.be.server.domain.ReservationRepositoryPort;
import kr.hhplus.be.server.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PayForReservationUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final BalanceService balanceService;
    private final ReservationRepositoryPort reservationRepository;

    @Transactional
    public Payment pay(UUID userId, UUID reservationId, Long amount) {
        Long balance = balanceService.getBalance(userId).balance();
        if (balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        Payment payment = Payment.create(userId, reservationId, amount);

        try {
            balanceService.deduct(userId, amount);

            reservationRepository.markAsPaid(reservationId);

            payment.complete();
        } catch (Exception e) {
            payment.fail();
            throw e;
        }

        return paymentRepository.save(payment);
    }

}
