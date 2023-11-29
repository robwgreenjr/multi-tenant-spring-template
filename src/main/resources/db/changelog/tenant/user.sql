CREATE TABLE IF NOT EXISTS tenant.user
(
    id         INTEGER GENERATED ALWAYS AS IDENTITY,
    tenant_id  uuid         NOT NULL REFERENCES internal.tenant (id) ON DELETE CASCADE,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(25)  NULL,
    created_on TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (email),
    UNIQUE (phone),
    UNIQUE (id)
);
