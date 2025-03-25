package com.example.bktask2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Представляет собой агрегированный отчет о звонках (UDR) для абонента.
 */
@Getter
@Setter
@AllArgsConstructor
public class Udr {

    /**
     * Номер абонента.
     */
    private String msisdn;

    /**
     * Информация по входящим звонкам.
     */
    private CallSummary incomingCall;

    /**
     * Информация по исходящим звонкам.
     */
    private CallSummary outcomingCall;

    /**
     * Внутренний класс для представления сводной информации о звонках (общее время).
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CallSummary {
        private String totalTime;
    }

    public Udr(String msisdn) {
        this.msisdn = msisdn;
        this.incomingCall = new CallSummary("00:00:00");
        this.outcomingCall = new CallSummary("00:00:00");
    }
}