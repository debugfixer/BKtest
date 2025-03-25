package com.example.bktask2.controller;

import com.example.bktask2.service.CdrReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * REST контроллер для работы с CDR и UDR.
 */
@RestController
@RequestMapping("/api/cdr")
public class CdrController {

    private final CdrReportService cdrReportService;

    @Autowired
    public CdrController(CdrReportService cdrReportService) {
        this.cdrReportService = cdrReportService;
    }

    /**
     * Инициирует генерацию CDR-отчета для заданного абонента за указанный период времени.
     * Возвращает UUID запроса.
     *
     * @param msisdn Номер абонента.
     * @param from   Начало периода времени (ISO 8601, например, 2025-02-15T10:00:00).
     * @param to     Конец периода времени (ISO 8601, например, 2025-02-20T18:00:00).
     * @return ResponseEntity с UUID запроса или текстом ошибки.
     */
    @GetMapping("/report")
    public ResponseEntity<String> generateCdrReport(
            @RequestParam("msisdn") String msisdn,
            @RequestParam("from") String from,
            @RequestParam("to") String to) {
        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(from);
            LocalDateTime toDateTime = LocalDateTime.parse(to);
            String reportUuid = cdrReportService.generateCdrReport(msisdn, fromDateTime, toDateTime);
            if (reportUuid != null) {
                return ResponseEntity.ok("CDR report generation initiated. Request UUID: " + reportUuid);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate CDR report.");
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date/time format. Please use ISO 8601 format (e.g., 2025-02-15T10:00:00).");
        }
    }

    // ... (ранее определенные методы UdrController могут быть перемещены сюда, если вы решили объединить контроллеры)
}