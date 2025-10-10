package kr.hhplus.be.server.repository;

import java.util.UUID;
import kr.hhplus.be.server.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<UserBalance, UUID> {
}
