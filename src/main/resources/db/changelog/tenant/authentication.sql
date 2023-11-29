CREATE TABLE IF NOT EXISTS tenant.authentication_user_password
(
    id                INTEGER GENERATED ALWAYS AS IDENTITY,
    tenant_id         uuid                     NOT NULL REFERENCES internal.tenant (id) ON DELETE CASCADE,
    user_id           INT4                     NOT NULL REFERENCES tenant.user (id) ON DELETE CASCADE,
    password          VARCHAR(60)              NULL,
    previous_password VARCHAR(60)              NULL,
    created_on        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on        TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id),
    UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS tenant.authentication_user_reset_password_token
(
    tenant_id  uuid                     NOT NULL REFERENCES internal.tenant (id) ON DELETE CASCADE,
    user_id    INT4                     NOT NULL REFERENCES tenant.user (id) ON DELETE CASCADE,
    token      uuid                              DEFAULT gen_random_uuid(),
    created_on TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (token),
    UNIQUE (user_id)
);
