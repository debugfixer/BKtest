package com.example.bktask2.service;

import com.example.bktask2.model.Cdr;
import com.example.bktask2.model.Subscriber;
import com.example.bktask2.model.Udr;
import com.example.bktask2.repository.CdrRepository;
import com.example.bktask2.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для формирования UDR (User Data Record) отчетов.
 */
@Service
public class UdrService {

    private static final Logger logger = LoggerFactory.getLogger(UdrService.class);
    private final CdrRepository cdrRepository;
    private final SubscriberRepository subscriberRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    public UdrService(CdrRepository cdrRepository, SubscriberRepository subscriberRepository) {
        this.cdrRepository = cdrRepository;
        this.subscriberRepository = subscriberRepository;
    }

    /**
     * Формирует UDR отчет для заданного абонента за указанный период времени.
     *
     * @param msisdn Номер абонента.
     * @param from   Начало периода времени (может быть null, если за весь период).
     * @param to     Конец периода времени (может быть null, если за весь период).
     * @return Объект {@link Udr} с информацией о звонках абонента.
     */
    public Optional<Udr> generateUdr(String msisdn, LocalDateTime from, LocalDateTime to) {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(msisdn);
        if (subscriberOptional.isEmpty()) {
            logger.warn("Абонент с номером {} не найден.", msisdn);
            return Optional.empty();
        }
        Subscriber subscriber = subscriberOptional.get();
        LocalDateTime startTime = from;
        LocalDateTime endTime = to == null ? LocalDateTime.now() : to;
        if (startTime == null) {
            startTime = LocalDateTime.MIN; // Или другая подходящая начальная дата
        }

        List<Cdr> cdrs = cdrRepository.findAllBySubscriberAndPeriod(subscriber, startTime, endTime);

        Duration incomingCallDuration = Duration.ZERO;
        Duration outcomingCallDuration = Duration.ZERO;

        for (Cdr cdr : cdrs) {
            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());
            if (cdr.getCallType().equals("02") && cdr.getCalledNumber().equals(subscriber)) {
                incomingCallDuration = incomingCallDuration.plus(callDuration);
            } else if (cdr.getCallType().equals("01") && cdr.getCallingNumber().equals(subscriber)) {
                outcomingCallDuration = outcomingCallDuration.plus(callDuration);
            }
        }

        String formattedIncomingTime = formatDuration(incomingCallDuration);
        String formattedOutcomingTime = formatDuration(outcomingCallDuration);

        return Optional.of(new Udr(msisdn, new Udr.CallSummary(formattedIncomingTime), new Udr.CallSummary(formattedOutcomingTime)));
    }

    /**
     * Формирует UDR отчет для заданного абонента за указанный месяц.
     *
     * @param msisdn Номер абонента.
     * @param year  Год.
     * @param month Месяц (1-12).
     * @return Объект {@link Udr} с информацией о звонках абонента за месяц.
     */
    public Optional<Udr> generateUdrForMonth(String msisdn, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime firstOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime lastOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);
        return generateUdr(msisdn, firstOfMonth, lastOfMonth);
    }

    /**
     * Формирует UDR отчет для всех абонентов за указанный месяц.
     *
     * @param year  Год.
     * @param month Месяц (1-12).
     * @return Список объектов {@link Udr} для всех абонентов.
     */
    public List<Udr> generateUdrForAllSubscribersForMonth(int year, int month) {
        List<Subscriber> allSubscribers = subscriberRepository.findAll();
        List<Udr> allUdrs = new ArrayList<>();
        for (Subscriber subscriber : allSubscribers) {
            generateUdrForMonth(subscriber.getMsisdn(), year, month)
                    .ifPresent(allUdrs::add);
        }
        return allUdrs;
    }

    /**
     * Форматирует объект {@link Duration} в строку формата "HH:mm:ss".
     *
     * @param duration Объект {@link Duration}.
     * @return Отформатированная строка времени.
     */
    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }
}