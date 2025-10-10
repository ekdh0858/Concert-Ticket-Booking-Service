package kr.hhplus.be.server.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Reservation {

    private UUID id;
    private UUID seatId;
    private UUID userId;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    private ReservationStatus status;

    public enum ReservationStatus {
        PENDING,   // 결제 대기
        CONFIRMED, // 결제 완료
        CANCELLED, // 만료/취소
    }

    public boolean isExpired(LocalDateTime now) {
        return now.isAfter(expiresAt);
    }

    public void confirmPayment() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
}
