package template.helpers;

import net.datafaker.Faker;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.UUID;

public class DatabaseSeeder {
    public void singleTable(JdbcTemplate jdbcTemplate, Integer count) {
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            String stringColumn = faker.name().firstName();
            Integer integerColumn = faker.number().randomDigit();
            Double doubleColumn = faker.number().randomDouble(4, 1, 1000);
            String textColumn = faker.chuckNorris().fact();
            Timestamp dateColumn = faker.date().birthday();

            String insertQuery =
                "INSERT INTO single_table (string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;
            }
        }
    }

    public void singleTable(JdbcTemplate jdbcTemplate, Integer count,
                            String commonValue, int numberOfCommonValues) {
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            String stringColumn = faker.name().firstName();

            if (numberOfCommonValues > i) {
                stringColumn += commonValue;
            } else {
                stringColumn = stringColumn.replace(commonValue, "");
            }

            Integer integerColumn = faker.number().randomDigit();
            Double doubleColumn = faker.number().randomDouble(4, 1, 1000);
            String textColumn = faker.chuckNorris().fact();
            Timestamp dateColumn = faker.date().birthday();

            String insertQuery =
                "INSERT INTO single_table (string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;
            }
        }
    }
}
