CREATE TABLE single_table
(
    id             INTEGER GENERATED ALWAYS AS IDENTITY,
    string_column  VARCHAR(255)             NULL,
    integer_column INT4                     NULL,
    double_column  double precision         NULL,
    text_column    TEXT                     NULL,
    uuid_column    uuid                     NULL,
    date_column    TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    created_on     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on     TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE double_table
(
    id              INTEGER GENERATED ALWAYS AS IDENTITY,
    single_table_id INT4                     NOT NULL REFERENCES single_table (id) ON DELETE CASCADE,
    string_column   VARCHAR(255)             NULL,
    integer_column  INT4                     NULL,
    double_column   double precision         NULL,
    text_column     TEXT                     NULL,
    uuid_column     uuid                     NULL,
    date_column     TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    created_on      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on      TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE triple_table
(
    id              INTEGER GENERATED ALWAYS AS IDENTITY,
    double_table_id INT4                     NOT NULL REFERENCES double_table (id) ON DELETE CASCADE,
    string_column   VARCHAR(255)             NULL,
    integer_column  INT4                     NULL,
    double_column   double precision         NULL,
    text_column     TEXT                     NULL,
    uuid_column     uuid                     NULL,
    date_column     TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    created_on      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on      TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE join_table
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY,
    name       VARCHAR(255)             NULL,
    created_on TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS single_table_join
(
    join_table_id   INT4                     NOT NULL REFERENCES join_table (id) ON DELETE CASCADE,
    single_table_id INT4                     NOT NULL REFERENCES single_table (id) ON DELETE CASCADE,
    created_on      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on      TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (join_table_id, single_table_id)
);