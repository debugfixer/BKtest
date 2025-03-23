import com.example.cdrservice.model.Cdr;
import com.example.cdrservice.repository.CdrRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CdrReportServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @InjectMocks
    private CdrReportService cdrReportService;

    @Test
    void generateCdrReport_shouldGenerateCsvFile() throws IOException {
        String msisdn = "79991112233";
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 31, 23, 59);

        List<Cdr> cdrs = List.of(createCdr(msisdn, "01"), createCdr(msisdn, "02"));
        when(cdrRepository.findByCallerMsisdnAndStartTimeBetween(msisdn, startDate, endDate)).thenReturn(cdrs);
        when(cdrRepository.findByCalleeMsisdnAndStartTimeBetween(msisdn, startDate, endDate)).thenReturn(List.of());

        String uuid = cdrReportService.generateCdrReport(msisdn, startDate, endDate);
        Path filePath = Paths.get("reports", msisdn + "_" + uuid + ".csv");

        assertTrue(Files.exists(filePath));
        Files.delete(filePath); // Удаляем созданный файл после теста
    }

    private Cdr createCdr(String msisdn, String callType) {
        Cdr cdr = new Cdr();
        cdr.setCallerMsisdn(msisdn);
        cdr.setCalleeMsisdn(msisdn);
        cdr.setCallType(callType);
        cdr.setStartTime(LocalDateTime.now());
        cdr.setEndTime(LocalDateTime.now().plusMinutes(10));
        return cdr;
    }
}