package template.helpers;

import net.datafaker.Faker;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseSeeder {
    public void single(JdbcTemplate jdbcTemplate, Integer count) {
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

    public void single(JdbcTemplate jdbcTemplate, Integer count,
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

    public void single(JdbcTemplate jdbcTemplate, Integer count,
                       String commonValue, int numberOfCommonValues,
                       String secondCommonValue,
                       int numberOfSecondCommonValues) {
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            String stringColumn = faker.name().firstName();
            String textColumn = faker.chuckNorris().fact();

            if (numberOfCommonValues > i) {
                stringColumn += commonValue;
            } else {
                stringColumn = stringColumn.replace(commonValue, "");
            }

            if (numberOfCommonValues <= i &&
                numberOfSecondCommonValues > (i - numberOfCommonValues)) {
                textColumn += secondCommonValue;
            } else {
                textColumn = stringColumn.replace(secondCommonValue, "");
            }

            Integer integerColumn = faker.number().randomDigit();
            Double doubleColumn = faker.number().randomDouble(4, 1, 1000);
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

    public void doubleTable(JdbcTemplate jdbcTemplate, Integer count,
                            String commonValue, int numberOfCommonValues) {
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            String stringColumn = faker.name().firstName();
            int integerColumn = faker.number().randomDigit();
            double doubleColumn = faker.number().randomDouble(4, 1, 1000);
            String textColumn = faker.chuckNorris().fact();
            Timestamp dateColumn = faker.date().birthday();

            if (numberOfCommonValues > i) {
                stringColumn += commonValue;
            } else {
                stringColumn = stringColumn.replace(commonValue, "");
            }

            String insertQuery =
                "INSERT INTO single_table (string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());

                String sql =
                    "SELECT * FROM single_table WHERE string_column = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, stringColumn);

                stringColumn = faker.name().firstName();
                integerColumn = faker.number().randomDigit();
                doubleColumn = faker.number().randomDouble(4, 1, 1000);
                textColumn = faker.chuckNorris().fact();
                dateColumn = faker.date().birthday();

                insertQuery =
                    "INSERT INTO double_table (single_table_id, string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;
            }
        }
    }

    public void tripleTable(JdbcTemplate jdbcTemplate, Integer count,
                            String commonValue, int numberOfCommonValues) {
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            String stringColumn = faker.name().firstName();
            int integerColumn = faker.number().randomDigit();
            double doubleColumn = faker.number().randomDouble(4, 1, 1000);
            String textColumn = faker.chuckNorris().fact();
            Timestamp dateColumn = faker.date().birthday();

            String deleteStringColumn = "";
            if (numberOfCommonValues > i) {
                stringColumn += commonValue;
            } else {
                stringColumn = stringColumn.replace(commonValue, "");
                deleteStringColumn = stringColumn;
            }

            String insertQuery =
                "INSERT INTO single_table (string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());

                String sql =
                    "SELECT * FROM single_table WHERE string_column = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, stringColumn);

                stringColumn = faker.name().firstName();
                integerColumn = faker.number().randomDigit();
                doubleColumn = faker.number().randomDouble(4, 1, 1000);
                textColumn = faker.chuckNorris().fact();
                dateColumn = faker.date().birthday();

                insertQuery =
                    "INSERT INTO double_table (single_table_id, string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());

                sql =
                    "SELECT * FROM double_table WHERE string_column = ?";
                singleObject =
                    jdbcTemplate.queryForList(sql, stringColumn);

                stringColumn = faker.name().firstName();
                integerColumn = faker.number().randomDigit();
                doubleColumn = faker.number().randomDouble(4, 1, 1000);
                textColumn = faker.chuckNorris().fact();
                dateColumn = faker.date().birthday();

                insertQuery =
                    "INSERT INTO triple_table (double_table_id, string_column, integer_column, double_column, text_column, date_column, uuid_column) VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    stringColumn, integerColumn,
                    doubleColumn,
                    textColumn, dateColumn, UUID.randomUUID());
            } catch (Exception exception) {
                if (!deleteStringColumn.isEmpty()) {
                    insertQuery =
                        "DELETE FROM single_table WHERE string_column = ?";

                    jdbcTemplate.update(insertQuery, deleteStringColumn);
                }

                // We could fail from unique values and will need to try again
                count++;
            }
        }
    }
}
