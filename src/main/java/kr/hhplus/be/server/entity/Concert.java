package kr.hhplus.be.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title; // 제목
    private String description; // 소개글
    private String director; // 감독

}
