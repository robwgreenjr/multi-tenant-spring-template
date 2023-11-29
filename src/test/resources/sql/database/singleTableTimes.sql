TRUNCATE TABLE single_table CASCADE;

INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_1', 1, 1.01, 'this is a test for text number 1',
        '2023-01-01 08:15:06.0000',
        '570c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_2', 2, 2.02, 'this is a test for text number 2',
        '2023-02-02 14:32:17.234',
        '267c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_3', 3, 3.03, 'this is a test for text number 3',
        '2023-03-03 20:53:38.591038',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_4', 4, 4.04, 'this is a test for text number 4',
        '2023-03-03 20:53:38.591110',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_5', 5, 5.05, 'this is a test for text number 5',
        '2023-03-03 20:53:38.591234',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');
