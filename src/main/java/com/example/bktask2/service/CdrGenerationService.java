package com.example.bktask2.service;

import com.example.bktask2.model.Cdr;
import com.example.bktask2.model.Subscriber;
import com.example.bktask2.repository.CdrRepository;
import com.example.bktask2.repository.SubscriberRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Сервис для генерации CDR-записей и инициализации списка абонентов.
 */
@Service
public class CdrGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(CdrGenerationService.class);
    private final SubscriberRepository subscriberRepository;
    private final CdrRepository cdrRepository;
    private final Random random = new Random();
    private List<Subscriber> subscribers;

    @Autowired
    public CdrGenerationService(SubscriberRepository subscriberRepository, CdrRepository cdrRepository) {
        this.subscriberRepository = subscriberRepository;
        this.cdrRepository = cdrRepository;
    }

    /**
     * Инициализирует список абонентов при запуске приложения, если он пуст.
     */
    @PostConstruct
    @Transactional
    public void initializeSubscribers() {
        if (subscriberRepository.count() == 0) {
            logger.info("Инициализация списка абонентов...");
            List<Subscriber> initialSubscribers = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                initialSubscribers.add(new Subscriber("7999" + String.format("%07d", i)));
            }
            subscriberRepository.saveAll(initialSubscribers);
            this.subscribers = subscriberRepository.findAll();
            logger.info("Список абонентов инициализирован: {}", this.subscribers);
        } else {
            this.subscribers = subscriberRepository.findAll();
            logger.info("Список абонентов уже существует: {}", this.subscribers);
        }
    }

    /**
     * Генерирует CDR записи за один год и сохраняет их в базу данных.
     */
    @Transactional
    public void generateCdrsForYear() {
        if (subscribers == null || subscribers.isEmpty()) {
            logger.warn("Список абонентов пуст, генерация CDR невозможна.");
            return;
        }

        logger.info("Начинается генерация CDR записей за текущий год...");
        LocalDateTime startOfYear = LocalDateTime.now().withMonth(Month.JANUARY.getValue()).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfYear = startOfYear.plusYears(1).minusNanos(1);

        for (Subscriber subscriber : subscribers) {
            generateCdrsForSubscriber(subscriber, startOfYear, endOfYear);
        }

        logger.info("Генерация CDR записей за год завершена.");
    }

    /**
     * Генерирует CDR записи для одного абонента в заданном временном диапазоне.
     *
     * @param subscriber  Абонент, для которого генерируются записи.
     * @param startTime   Начальное время периода.
     * @param endTime     Конечное время периода.
     */
    private void generateCdrsForSubscriber(Subscriber subscriber, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime currentCallTime = startTime;
        while (currentCallTime.isBefore(endTime)) {
            // Случайное количество звонков в день
            int numberOfCalls = random.nextInt(5);
            for (int i = 0; i < numberOfCalls; i++) {
                if (currentCallTime.isAfter(endTime)) {
                    break;
                }
                // Случайный тип вызова (01 - исходящий, 02 - входящий)
                String callType = random.nextInt(2) == 0 ? "01" : "02";

                Subscriber calledSubscriber = subscribers.get(random.nextInt(subscribers.size()));
                // Исключаем исходящий звонок на самого себя
                if (callType.equals("01") && calledSubscriber.equals(subscriber)) {
                    continue;
                }

                // Случайная продолжительность звонка (от 1 секунды до 10 минут)
                int durationSeconds = random.nextInt(600) + 1;
                LocalDateTime callEndTime = currentCallTime.plusSeconds(durationSeconds);

                if (callEndTime.isAfter(endTime)) {
                    callEndTime = endTime;
                }

                Cdr cdr = new Cdr();
                cdr.setCallType(callType);
                cdr.setStartTime(currentCallTime);
                cdr.setEndTime(callEndTime);
                if (callType.equals("01")) {
                    cdr.setCallingNumber(subscriber);
                    cdr.setCalledNumber(calledSubscriber);
                } else {
                    cdr.setCallingNumber(calledSubscriber);
                    cdr.setCalledNumber(subscriber);
                }
                cdrRepository.save(cdr);

                // Перемещаем время следующего звонка на случайный интервал (от 5 минут до 2 часов)
                currentCallTime = callEndTime.plusMinutes(random.nextInt(115) + 5);
            }
            // Переходим к следующему дню
            currentCallTime = currentCallTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        logger.info("Сгенерировано CDR записей для абонента {} за год.", subscriber.getMsisdn());
    }
}