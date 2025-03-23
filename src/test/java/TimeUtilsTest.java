import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeUtilsTest {

    @Test
    void formatDuration_shouldFormatDurationCorrectly() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime endTime = startTime.plusHours(2).plusMinutes(30).plusSeconds(15);

        String formattedDuration = TimeUtils.formatDuration(startTime, endTime);

        assertEquals("02:30:15", formattedDuration);
    }
}