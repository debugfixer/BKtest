import com.example.cdrservice.model.Cdr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CdrRepositoryIntegrationTest {

    @Autowired
    private CdrRepository cdrRepository;

    @Test
    void findByCallerMsisdnAndStartTimeBetween_shouldReturnCorrectCdrs() {
        String msisdn = "79991112233";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);

        Cdr cdr1 = new Cdr();
        cdr1.setCallerMsisdn(msisdn);
        cdr1.setCalleeMsisdn("79992224455");
        cdr1.setCallType("01");
        cdr1.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        cdr1.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 10));
        cdrRepository.save(cdr1);

        List<Cdr> foundCdrs = cdrRepository.findByCallerMsisdnAndStartTimeBetween(msisdn, startDate, endDate);

        assertEquals(1, foundCdrs.size());
        assertEquals(msisdn, foundCdrs.get(0).getCallerMsisdn());
    }
}