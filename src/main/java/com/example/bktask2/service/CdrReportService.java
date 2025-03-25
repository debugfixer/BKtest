package com.example.bktask2.service;

import com.example.bktask2.model.Cdr;
import com.example.bktask2.model.Subscriber;
import com.example.bktask2.repository.CdrRepository;
import com.example.bktask2.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для формирования и сохранения CDR-отчетов.
 */
@Service
public class CdrReportService {

    private static final Logger logger = LoggerFactory.getLogger(CdrReportService.class);
    private final CdrRepository cdrRepository;
    private final SubscriberRepository subscriberRepository;
    private static final DateTimeFormatter CDR_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String reportDirectory;

    @Autowired
    public CdrReportService(CdrRepository cdrRepository, SubscriberRepository subscriberRepository, Environment env) {
        this.cdrRepository = cdrRepository;
        this.subscriberRepository = subscriberRepository;
        this.reportDirectory = env.getProperty("cdr.report.directory", "/reports");
        createReportDirectory();
    }

    /**
     * Создает директорию для отчетов, если она не существует.
     */
    private void createReportDirectory() {
        Path path = Paths.get(reportDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Директория для отчетов создана: {}", reportDirectory);
            } catch (IOException e) {
                logger.error("Не удалось создать директорию для отчетов: {}", reportDirectory, e);
            }
        }
    }

    /**
     * Генерирует CDR-отчет для заданного абонента за указанный период времени и сохраняет его в файл.
     *
     * @param msisdn  Номер абонента.
     * @param from    Начало периода времени (ISO 8601).
     * @param to      Конец периода времени (ISO 8601).
     * @return Уникальный UUID запроса или null в случае ошибки.
     */
    @Transactional(readOnly = true)
    public String generateCdrReport(String msisdn, LocalDateTime from, LocalDateTime to) {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findById(msisdn);
        if (subscriberOptional.isEmpty()) {
            logger.warn("Абонент с номером {} не найден.", msisdn);
            return null;
        }
        Subscriber subscriber = subscriberOptional.get();
        List<Cdr> cdrs = cdrRepository.findAllBySubscriberAndPeriod(subscriber, from, to);
        UUID reportUuid = UUID.randomUUID();
        String filename = String.format("%s_%s.csv", msisdn, reportUuid);
        Path filePath = Paths.get(reportDirectory, filename);

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            for (Cdr cdr : cdrs) {
                writer.write(String.format("%s,%s,%s,%s,%s%n",
                        cdr.getCallType(),
                        cdr.getCallingNumber().getMsisdn(),
                        cdr.getCalledNumber().getMsisdn(),
                        cdr.getStartTime().format(CDR_DATE_TIME_FORMATTER),
                        cdr.getEndTime().format(CDR_DATE_TIME_FORMATTER)));
            }
            logger.info("CDR-отчет для абонента {} за период с {} по {} успешно сгенерирован: {}",
                    msisdn, from, to, filePath.toAbsolutePath());
            return reportUuid.toString();
        } catch (IOException e) {
            logger.error("Ошибка при записи CDR-отчета в файл {}: {}", filePath.toAbsolutePath(), e.getMessage());
            return null;
        }
    }
}