package template.global.utilities;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTimeSpecialistTest {
    private final TimeSpecialist simpleTimeSpecialist = new SimpleTimeSpecialist();

    @Test
    public void given180_whenIntegerToHoursAndMinutesCalled_shouldReturn3Hours() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(180);

        Assertions.assertEquals("3 hours", actual);
    }

    @Test
    public void given156_whenIntegerToHoursCalled_shouldReturn2HoursAnd36Minutes() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(156);

        Assertions.assertEquals("2 hours and 36 minutes", actual);
    }

    @Test
    public void given60_whenIntegerToHoursCalled_shouldReturn1Hour() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(60);

        Assertions.assertEquals("1 hour", actual);
    }

    @Test
    public void given68_whenIntegerToHoursCalled_shouldReturn1HourAnd8Minutes() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(68);

        Assertions.assertEquals("1 hour and 8 minutes", actual);
    }

    @Test
    public void given61_whenIntegerToHoursCalled_shouldReturn1HourAnd1Minute() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(61);

        Assertions.assertEquals("1 hour and 1 minute", actual);
    }

    @Test
    public void given30_whenIntegerToHoursCalled_shouldReturn30Minutes() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(30);

        Assertions.assertEquals("30 minutes", actual);
    }

    @Test
    public void given1_whenIntegerToHoursCalled_shouldReturn1Minute() {
        String actual = simpleTimeSpecialist.integerToHoursAndMinutes(1);

        Assertions.assertEquals("1 minute", actual);
    }
}
