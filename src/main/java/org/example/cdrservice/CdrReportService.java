package org.example.cdrservice;

import com.example.cdrservice.Cdr;
import com.example.cdrservice.CdrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для генерации CDR отчетов.
 */
@Service
@RequiredArgsConstructor
public class CdrReportService {

    private final CdrRepository cdrRepository;
    private static final String REPORTS_DIRECTORY = "reports";

    /**
     * Генерирует CDR отчет по абоненту за указанный период.
     *
     * @param msisdn    Номер абонента.
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return UUID запроса.
     */
    public String generateCdrReport(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cdr> cdrs = cdrRepository.findByCallerMsisdnAndStartTimeBetween(msisdn, startDate, endDate);
        cdrs.addAll(cdrRepository.findByCalleeMsisdnAndStartTimeBetween(msisdn, startDate, endDate));
        cdrs.sort(java.util.Comparator.comparing(Cdr::getStartTime)); // Сортировка по времени

        String uuid = UUID.randomUUID().toString();
        String filename = msisdn + "_" + uuid + ".csv";
        Path filePath = Paths.get(REPORTS_DIRECTORY, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write("CallType,CallerMsisdn,CalleeMsisdn,StartTime,EndTime\n"); // Заголовок
            for (Cdr cdr : cdrs) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                        cdr.getCallType(), cdr.getCallerMsisdn(), cdr.getCalleeMsisdn(),
                        cdr.getStartTime(), cdr.getEndTime()));
            }
        } catch (IOException e) {
            // TODO: Обработка ошибок записи в файл
            return "Error: " + e.getMessage();
        }
        return uuid;
    }
}