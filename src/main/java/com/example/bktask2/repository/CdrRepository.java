package com.example.bktask2.repository;

import com.example.bktask2.model.Cdr;
import com.example.bktask2.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Cdr}.
 */
@Repository
public interface CdrRepository extends JpaRepository<Cdr, Long> {

    /**
     * Возвращает список CDR-записей для заданного абонента за указанный период времени.
     *
     * @param subscriber Номер абонента.
     * @param startTime  Начальное время периода.
     * @param endTime    Конечное время периода.
     * @return Список CDR-записей.
     */
    List<Cdr> findByCallingNumberAndStartTimeBetweenOrderByStartTime(Subscriber subscriber, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Возвращает список CDR-записей, где заданный абонент является вызываемым, за указанный период времени.
     *
     * @param subscriber Номер абонента.
     * @param startTime  Начальное время периода.
     * @param endTime    Конечное время периода.
     * @return Список CDR-записей.
     */
    List<Cdr> findByCalledNumberAndStartTimeBetweenOrderByStartTime(Subscriber subscriber, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Возвращает все CDR-записи за указанный период времени.
     *
     * @param startTime Начальное время периода.
     * @param endTime   Конечное время периода.
     * @return Список CDR-записей.
     */
    List<Cdr> findByStartTimeBetweenOrderByStartTime(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Возвращает все CDR-записи для заданного абонента за указанный период времени.
     * Использует @Query для более сложного запроса, объединяющего условия по вызывающему и вызываемому абоненту.
     *
     * @param subscriber Номер абонента.
     * @param startTime  Начальное время периода.
     * @param endTime    Конечное время периода.
     * @return Список CDR-записей.
     */
    @Query("SELECT c FROM Cdr c WHERE " +
            "((c.callingNumber = :subscriber) OR (c.calledNumber = :subscriber)) AND " +
            "c.startTime BETWEEN :startTime AND :endTime " +
            "ORDER BY c.startTime")
    List<Cdr> findAllBySubscriberAndPeriod(@Param("subscriber") Subscriber subscriber,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
}