package learning.jdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeTest {

    @Test
    public void makeDate() {
        int year = 1987;
        int month = 6;
        int dayOfMonth = 17;
        int hour = 10;
        int minute = 30;
        LocalDateTime date = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        assertThat(date.getYear()).isEqualTo(year);
        assertThat(date.getMonth().getValue()).isEqualTo(month);
        assertThat(date.getDayOfMonth()).isEqualTo(dayOfMonth);
    }

}