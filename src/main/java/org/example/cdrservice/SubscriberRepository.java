package org.example.cdrservice;

import com.example.cdrservice.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с абонентами.
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
}