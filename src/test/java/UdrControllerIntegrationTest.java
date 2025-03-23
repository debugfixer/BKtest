import com.example.cdrservice.model.Cdr;
import com.example.cdrservice.repository.CdrRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UdrControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CdrRepository cdrRepository;

    @BeforeEach
    void setUp() {
        cdrRepository.deleteAll(); // Очищаем базу данных перед каждым тестом
        createTestData();
    }

    @Test
    void getUdr_shouldReturnUdrReport() throws Exception {
        mockMvc.perform(get("/udr/79991112233?startDate=2025-01-01T00:00:00&endDate=2025-01-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private void createTestData() {
        Cdr cdr1 = new Cdr();
        cdr1.setCallerMsisdn("79991112233");
        cdr1.setCalleeMsisdn("79992224455");
        cdr1.setCallType("01");
        cdr1.setStartTime(LocalDateTime.of(2025, 1, 1, 10, 0));
        cdr1.setEndTime(LocalDateTime.of(2025, 1, 1, 10, 10));
        cdrRepository.save(cdr1);

        Cdr cdr2 = new Cdr();
        cdr2.setCallerMsisdn("79992224455");
        cdr2.setCalleeMsisdn("79991112233");
        cdr2.setCallType("02");
        cdr2.setStartTime(LocalDateTime.of(2025, 1, 1, 11, 0));
        cdr2.setEndTime(LocalDateTime.of(2025, 1, 1, 11, 20));
        cdrRepository.save(cdr2);
    }
}