package org.example.cdrservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Модель данных для CDR (Call Data Record).
 */
@Entity
@Data
public class Cdr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String callType; // Тип вызова (01 - исходящий, 02 - входящий)
    private String callerMsisdn; // Номер абонента, инициирующего звонок
    private String calleeMsisdn; // Номер абонента, принимающего звонок
    private LocalDateTime startTime; // Дата и время начала звонка
    private LocalDateTime endTime; // Дата и время окончания звонка
}