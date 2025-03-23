package org.example.cdrservice;

import com.example.cdrservice.Cdr;
import com.example.cdrservice.CdrRepository;
import com.example.cdrservice.Subscriber;
import com.example.cdrservice.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Сервис для генерации CDR записей.
 */
@Service
@RequiredArgsConstructor
public class CdrGeneratorService {

    private final CdrRepository cdrRepository;
    private final SubscriberRepository subscriberRepository;
    private final Random random = new Random();

    /**
     * Генерирует CDR записи за указанный год.
     *
     * @param year Год для генерации CDR записей.
     */
    public void generateCdrRecords(int year) {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year + 1, 1, 1, 0, 0).minusSeconds(1);

        LocalDateTime currentTime = startDate;
        while (currentTime.isBefore(endDate)) {
            Cdr cdr = generateRandomCdr(subscribers, currentTime);
            cdrRepository.save(cdr);
            currentTime = currentTime.plusMinutes(random.nextInt(60 * 24)); // Случайное время между звонками
        }
    }

    /**
     * Генерирует случайную CDR запись.
     *
     * @param subscribers Список абонентов.
     * @param startTime   Время начала звонка.
     * @return Случайная CDR запись.
     */
    private Cdr generateRandomCdr(List<Subscriber> subscribers, LocalDateTime startTime) {
        Cdr cdr = new Cdr();
        cdr.setCallType(random.nextBoolean() ? "01" : "02");
        cdr.setCallerMsisdn(subscribers.get(random.nextInt(subscribers.size())).getMsisdn());
        cdr.setCalleeMsisdn(subscribers.get(random.nextInt(subscribers.size())).getMsisdn());
        cdr.setStartTime(startTime);
        cdr.setEndTime(startTime.plusMinutes(random.nextInt(60))); // Случайная продолжительность звонка
        return cdr;
    }
}