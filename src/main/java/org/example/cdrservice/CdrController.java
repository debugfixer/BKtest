package org.example.cdrservice;

import com.example.cdrservice.CdrReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST контроллер для генерации CDR отчетов.
 */
@RestController
@RequiredArgsConstructor
public class CdrController {

    private final CdrReportService cdrReportService;

    /**
     * Инициирует генерацию CDR отчета по абоненту за указанный период.
     *
     * @param msisdn    Номер абонента.
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return UUID запроса.
     */
    @GetMapping("/cdr/{msisdn}/report")
    public String generateCdrReport(
            @PathVariable String msisdn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return cdrReportService.generateCdrReport(msisdn, startDate, endDate);
    }
}