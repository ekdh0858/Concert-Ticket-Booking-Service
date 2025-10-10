package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.Payment;
import kr.hhplus.be.server.domain.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryPort {

    private final PaymentJpaRepository jpaRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity saved = jpaRepository.save(PaymentMapper.toEntity(payment));
        return PaymentMapper.toDomain(saved);
    }
}
