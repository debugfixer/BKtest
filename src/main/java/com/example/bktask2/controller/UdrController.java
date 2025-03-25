package com.example.bktask2.controller;

import com.example.bktask2.model.Udr;
import com.example.bktask2.service.UdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для работы с UDR (User Data Record).
 */
@RestController
@RequestMapping("/api/udr")
public class UdrController {

    private final UdrService udrService;

    @Autowired
    public UdrController(UdrService udrService) {
        this.udrService = udrService;
    }

    /**
     * Возвращает UDR запись для заданного абонента за указанный месяц или за весь период.
     *
     * @param msisdn Номер абонента.
     * @param year   Год для формирования отчета (необязательный параметр).
     * @param month  Месяц для формирования отчета (необязательный параметр, 1-12).
     * @return ResponseEntity с UDR объектом или статусом 404, если абонент не найден.
     */
    @GetMapping("/{msisdn}")
    public ResponseEntity<Udr> getUdrByMsisdn(
            @PathVariable String msisdn,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {

        Optional<Udr> udr;
        if (year != null && month != null) {
            udr = udrService.generateUdrForMonth(msisdn, year, month);
        } else {
            udr = udrService.generateUdr(msisdn, null, null); // За весь период
        }

        return udr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Возвращает UDR записи для всех абонентов за указанный месяц.
     *
     * @param year  Год для формирования отчета.
     * @param month Месяц для формирования отчета (1-12).
     * @return ResponseEntity со списком UDR объектов.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Udr>> getAllUdrsForMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        List<Udr> udrs = udrService.generateUdrForAllSubscribersForMonth(year, month);
        return ResponseEntity.ok(udrs);
    }
}