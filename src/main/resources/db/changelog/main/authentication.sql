CREATE TABLE IF NOT EXISTS internal.authentication_user_password
(
    id                INTEGER GENERATED ALWAYS AS IDENTITY,
    user_id           INT4                     NOT NULL REFERENCES internal.user (id) ON DELETE CASCADE,
    password          VARCHAR(60)              NULL,
    previous_password VARCHAR(60)              NULL,
    created_on        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on        TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id),
    UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS internal.authentication_user_reset_password_token
(
    user_id    INT4                     NOT NULL REFERENCES internal.user (id) ON DELETE CASCADE,
    token      uuid                              DEFAULT gen_random_uuid(),
    created_on TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (token),
    UNIQUE (user_id)
);