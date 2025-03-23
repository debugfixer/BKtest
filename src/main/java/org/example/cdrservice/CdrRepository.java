package org.example.cdrservice;

import com.example.cdrservice.Cdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с CDR.
 */
@Repository
public interface CdrRepository extends JpaRepository<Cdr, Long> {

    List<Cdr> findByCallerMsisdnAndStartTimeBetween(String callerMsisdn, LocalDateTime startTime, LocalDateTime endTime);

    List<Cdr> findByCalleeMsisdnAndStartTimeBetween(String calleeMsisdn, LocalDateTime startTime, LocalDateTime endTime);

    List<Cdr> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}