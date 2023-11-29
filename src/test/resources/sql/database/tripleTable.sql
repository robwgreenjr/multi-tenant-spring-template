TRUNCATE TABLE single_table CASCADE;

INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_1', 1, 1.01, 'this is a test for text number 1', '2021-01-01',
        '570c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_2', 2, 2.02, 'this is a test for text number 2', '2022-02-02',
        '267c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO single_table (string_column, integer_column, double_column,
                          text_column, date_column, uuid_column)
VALUES ('test_3', 3, 3.03, 'this is a test for text number 3', '2023-03-03',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');

INSERT INTO double_table (single_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM single_table WHERE string_column = 'test_1'),
        'nested_test_1', 1, 1.01, 'this is a test for text number 1',
        '2021-01-01',
        '570c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO double_table (single_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM single_table WHERE string_column = 'test_2'),
        'nested_test_2', 2, 2.02, 'this is a test for text number 2',
        '2022-02-02',
        '267c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO double_table (single_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM single_table WHERE string_column = 'test_3'),
        'nested_test_3', 3, 3.03, 'this is a test for text number 3',
        '2023-03-03',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');

INSERT INTO triple_table (double_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM double_table WHERE string_column = 'nested_test_1'),
        'nested_nested_test_1', 1, 1.01, 'this is a test for text number 1',
        '2021-01-01',
        '570c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO triple_table (double_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM double_table WHERE string_column = 'nested_test_2'),
        'nested_nested_test_2', 2, 2.02, 'this is a test for text number 2',
        '2022-02-02',
        '267c9397-1b91-4f46-9e91-8c8f030a01e0');
INSERT INTO triple_table (double_table_id, string_column, integer_column,
                          double_column,
                          text_column, date_column, uuid_column)
VALUES ((SELECT id FROM double_table WHERE string_column = 'nested_test_3'),
        'nested_nested_test_3', 3, 3.03, 'this is a test for text number 3',
        '2023-03-03',
        '834c9397-1b91-4f46-9e91-8c8f030a01e0');
