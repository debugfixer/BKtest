import com.example.cdrservice.model.Cdr;
import com.example.cdrservice.model.Subscriber;
import com.example.cdrservice.repository.CdrRepository;
import com.example.cdrservice.repository.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CdrGeneratorServiceTest {

    @Mock
    private CdrRepository cdrRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private CdrGeneratorService cdrGeneratorService;

    @Test
    void generateCdrRecords_shouldGenerateAndSaveCdrs() {
        List<Subscriber> subscribers = List.of(new Subscriber(), new Subscriber());
        when(subscriberRepository.findAll()).thenReturn(subscribers);

        cdrGeneratorService.generateCdrRecords(2025);

        verify(cdrRepository, atLeast(365)).save(any(Cdr.class)); // Проверяем, что сохранение CDR вызывалось хотя бы 365 раз
    }
}