package com.example.bktask2.repository;

import com.example.bktask2.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностями {@link Subscriber}.
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    // Spring Data JPA автоматически предоставляет базовые CRUD операции
    // Нет необходимости определять дополнительные методы на данном этапе
}