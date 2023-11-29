package template.global.utilities;

public interface StringParser {

    boolean isValidDateTimeHour(String dateTime);

    boolean isValidDateTimeHourAndMinutes(String dateTime);

    boolean isValidDateTimeHourAndMinutesAndSeconds(String dateTime);

    boolean isValidDateTimeHourAndMinutesAndSecondsAndMilliseconds(
        String dateTime);

    boolean isISO8601(String input);
}
