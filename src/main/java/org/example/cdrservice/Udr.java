package org.example.cdrservice;

import lombok.Data;

/**
 * Модель данных для UDR (Usage Data Record).
 */
@Data
public class Udr {

    private String msisdn; // Номер абонента
    private CallInfo incomingCall; // Информация о входящих вызовах
    private CallInfo outgoingCall; // Информация об исходящих вызовах

    @Data
    public static class CallInfo {
        private String totalTime; // Общая продолжительность звонков
    }
}