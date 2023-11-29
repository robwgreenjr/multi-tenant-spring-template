package template.global.utilities;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringParserImpl implements StringParser {
    private static final Pattern ISO8601_PATTERN = Pattern.compile(
        "^\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,9})?(Z|[+-]\\d{2}:\\d{2})?)?$"
    );
    
    @Override
    public boolean isValidDateTimeHour(String dateTime) {
        final Pattern pattern =
            Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d",
                Pattern.CASE_INSENSITIVE);

        final Matcher matcher = pattern.matcher(dateTime);

        return matcher.matches();
    }

    @Override
    public boolean isValidDateTimeHourAndMinutes(String dateTime) {
        final Pattern pattern =
            Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d",
                Pattern.CASE_INSENSITIVE);

        final Matcher matcher = pattern.matcher(dateTime);

        return matcher.matches();
    }

    @Override
    public boolean isValidDateTimeHourAndMinutesAndSeconds(String dateTime) {
        final Pattern pattern =
            Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d",
                Pattern.CASE_INSENSITIVE);

        final Matcher matcher = pattern.matcher(dateTime);

        return matcher.matches();
    }

    @Override
    public boolean isValidDateTimeHourAndMinutesAndSecondsAndMilliseconds(
        String dateTime) {
        final Pattern pattern =
            Pattern.compile(
                "\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d{1,6})?",
                Pattern.CASE_INSENSITIVE);

        final Matcher matcher = pattern.matcher(dateTime);

        return matcher.matches();
    }

    @Override
    public boolean isISO8601(String input) {
        Matcher matcher = ISO8601_PATTERN.matcher(input);
        return matcher.matches();
    }
}
