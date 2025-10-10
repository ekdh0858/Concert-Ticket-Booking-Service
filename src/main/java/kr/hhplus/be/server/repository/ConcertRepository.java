package kr.hhplus.be.server.repository;

import java.util.UUID;
import kr.hhplus.be.server.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, UUID> {

}
