package com.example.bktask2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Представляет запись о вызове (Call Detail Record).
 */
@Entity
@Table(name = "cdr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cdr {

    /**
     * Уникальный идентификатор записи CDR.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Тип вызова (01 - исходящий, 02 - входящий).
     */
    private String callType;

    /**
     * Абонент, инициировавший звонок.
     */
    @ManyToOne
    private Subscriber callingNumber;

    /**
     * Абонент, принявший звонок.
     */
    @ManyToOne
    private Subscriber calledNumber;

    /**
     * Дата и время начала звонка.
     */
    private LocalDateTime startTime;

    /**
     * Дата и время окончания звонка.
     */
    private LocalDateTime endTime;
}