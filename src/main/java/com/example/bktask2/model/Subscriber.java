package com.example.bktask2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Представляет абонента сотовой сети.
 */
@Entity
@Table(name = "subscribers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {

    /**
     * Номер абонента (MSISDN), является уникальным идентификатором.
     */
    @Id
    private String msisdn;

    @Override
    public String toString() {
        return msisdn;
    }
}