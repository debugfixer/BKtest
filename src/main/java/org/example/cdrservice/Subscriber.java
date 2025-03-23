package org.example.cdrservice;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Модель данных для абонента.
 */
@Entity
@Data
public class Subscriber {

    @Id
    private String msisdn; // Номер абонента
}