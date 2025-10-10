package kr.hhplus.be.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id")
    private ConcertSchedule concertSchedule;

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // AVAILABLE, RESERVED, SOLD

    public enum SeatStatus {
        AVAILABLE,
        RESERVED,
        SOLD
    }

    public void reserve() {
        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약되었거나 판매된 좌석입니다.");
        }
        this.status = SeatStatus.RESERVED;
    }

    public void sell() {
        if (this.status != SeatStatus.RESERVED) {
            throw new IllegalStateException("좌석이 예약되지 않았습니다. 판매 불가.");
        }
        this.status = SeatStatus.SOLD;
    }

    public void cancelReservation() {
        if (this.status == SeatStatus.RESERVED) {
            this.status = SeatStatus.AVAILABLE;
        }
    }
}
