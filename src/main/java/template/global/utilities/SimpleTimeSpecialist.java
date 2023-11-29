package template.global.utilities;

import org.springframework.stereotype.Service;

@Service
public class SimpleTimeSpecialist implements TimeSpecialist {
    @Override
    public String integerToHoursAndMinutes(Integer time) {
        String result = "";

        int hours = time / 60;
        int minutes = time % 60;

        if (hours == 1) {
            result += "1 hour";
        } else if (hours != 0) {
            result += hours + " hours";
        }

        if (hours != 0 && minutes != 0) {
            result += " and ";
        }

        if (minutes == 1) {
            result += "1 minute";
        } else if (minutes != 0) {
            result += minutes + " minutes";
        }

        return result;
    }
}
