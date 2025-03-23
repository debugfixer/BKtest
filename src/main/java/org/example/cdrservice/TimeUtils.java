package org.example.cdrservice;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Утилитарный класс для работы со временем.
 */
public class TimeUtils {

    /**
     * Форматирует продолжительность звонка в строку "HH:mm:ss".
     *
     * @param startTime Время начала звонка.
     * @param endTime   Время окончания звонка.
     * @return Строка с продолжительностью звонка.
     */
    public static String formatDuration(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}