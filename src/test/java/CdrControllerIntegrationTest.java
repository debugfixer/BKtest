import com.example.cdrservice.model.Cdr;
import com.example.cdrservice.repository.CdrRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CdrControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CdrRepository cdrRepository;

    @BeforeEach
    void setUp() {
        cdrRepository.deleteAll();
        createTestData();
    }

    @Test
    void generateCdrReport_shouldReturnUuid() throws Exception {
        mockMvc.perform(get("/cdr/79991112233/report?startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59"))
                .andExpect(status().isOk());
    }

    private void createTestData() {
        Cdr cdr1 = new Cdr();
        cdr1.setCallerMsisdn("79991112233");
        cdr1.setCalleeMsisdn("79992224455");
        cdr1.setCallType("01");
        cdr1.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        cdr1.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 10));
        cdrRepository.save(cdr1);
    }
}