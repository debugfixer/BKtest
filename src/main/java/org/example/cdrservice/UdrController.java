package org.example.cdrservice;

import com.example.cdrservice.Udr;
import com.example.cdrservice.UdrService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST контроллер для работы с UDR отчетами.
 */
@RestController
@RequiredArgsConstructor
public class UdrController {

    private final UdrService udrService;

    /**
     * Возвращает UDR отчет по абоненту за указанный период.
     *
     * @param msisdn    Номер абонента.
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return UDR отчет.
     */
    @GetMapping("/udr/{msisdn}")
    public Udr getUdr(
            @PathVariable String msisdn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return udrService.generateUdr(msisdn, startDate, endDate);
    }

    /**
     * Возвращает UDR отчеты по всем абонентам за указанный период.
     *
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return Список UDR отчетов.
     */
    @GetMapping("/udr")
    public List<Udr> getAllUdrs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return udrService.generateAllUdrs(startDate, endDate);
    }
}