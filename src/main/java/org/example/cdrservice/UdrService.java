package org.example.cdrservice;

import com.example.cdrservice.Cdr;
import com.example.cdrservice.CdrRepository;
import com.example.cdrservice.TimeUtils;
import com.example.cdrservice.Udr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для формирования UDR отчетов.
 */
@Service
@RequiredArgsConstructor
public class UdrService {

    private final CdrRepository cdrRepository;

    /**
     * Формирует UDR отчет по абоненту за указанный период.
     *
     * @param msisdn    Номер абонента.
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return UDR отчет.
     */
    public Udr generateUdr(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<Cdr> incomingCalls = cdrRepository.findByCalleeMsisdnAndStartTimeBetween(msisdn, startDate, endDate);
        List<Cdr> outgoingCalls = cdrRepository.findByCallerMsisdnAndStartTimeBetween(msisdn, startDate, endDate);

        Udr udr = new Udr();
        udr.setMsisdn(msisdn);
        udr.setIncomingCall(calculateTotalTime(incomingCalls));
        udr.setOutgoingCall(calculateTotalTime(outgoingCalls));

        return udr;
    }

    /**
     * Формирует UDR отчет по всем абонентам за указанный период.
     *
     * @param startDate Начало периода.
     * @param endDate   Конец периода.
     * @return Список UDR отчетов.
     */
    public List<Udr> generateAllUdrs(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Реализовать логику получения всех абонентов и формирования UDR по каждому
        return null;
    }

    /**
     * Рассчитывает общую продолжительность звонков.
     *
     * @param calls Список CDR записей.
     * @return Информация о продолжительности звонков.
     */
    private Udr.CallInfo calculateTotalTime(List<Cdr> calls) {
        Udr.CallInfo callInfo = new Udr.CallInfo();
        long totalSeconds = calls.stream()
                .mapToLong(cdr -> java.time.Duration.between(cdr.getStartTime(), cdr.getEndTime()).getSeconds())
                .sum();
        callInfo.setTotalTime(TimeUtils.formatDuration(LocalDateTime.MIN, LocalDateTime.MIN.plusSeconds(totalSeconds)));
        return callInfo;
    }
}