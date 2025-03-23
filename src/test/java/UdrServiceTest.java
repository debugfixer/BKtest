import com.example.cdrservice.model.Cdr;
import com.example.cdrservice.model.Udr;
import com.example.cdrservice.repository.CdrRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UdrServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @InjectMocks
    private UdrService udrService;

    @Test
    void generateUdr_shouldCalculateTotalTimeCorrectly() {
        String msisdn = "79991112233";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);

        List<Cdr> incomingCalls = List.of(createCdr(msisdn, "02", 10), createCdr(msisdn, "02", 20));
        List<Cdr> outgoingCalls = List.of(createCdr(msisdn, "01", 5), createCdr(msisdn, "01", 15));

        when(cdrRepository.findByCalleeMsisdnAndStartTimeBetween(msisdn, startDate, endDate)).thenReturn(incomingCalls);
        when(cdrRepository.findByCallerMsisdnAndStartTimeBetween(msisdn, startDate, endDate)).thenReturn(outgoingCalls);

        Udr udr = udrService.generateUdr(msisdn, startDate, endDate);

        assertEquals("00:30:00", udr.getIncomingCall().getTotalTime());
        assertEquals("00:20:00", udr.getOutgoingCall().getTotalTime());
    }

    private Cdr createCdr(String msisdn, String callType, int minutes) {
        Cdr cdr = new Cdr();
        cdr.setCallerMsisdn(msisdn);
        cdr.setCalleeMsisdn(msisdn);
        cdr.setCallType(callType);
        LocalDateTime startTime = LocalDateTime.now();
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusMinutes(minutes));
        return cdr;
    }
}