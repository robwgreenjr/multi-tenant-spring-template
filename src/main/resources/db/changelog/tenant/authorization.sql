CREATE TABLE IF NOT EXISTS tenant.authorization_role
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY,
    tenant_id   uuid                     NOT NULL REFERENCES internal.tenant (id) ON DELETE CASCADE,
    name        VARCHAR(100)             NOT NULL,
    description TEXT                     NULL,
    created_on  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on  TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (tenant_id, name),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS tenant.authorization_permission
(
    id          INTEGER GENERATED ALWAYS AS IDENTITY,
    tenant_id   uuid                     NOT NULL REFERENCES internal.tenant (id) ON DELETE CASCADE,
    name        VARCHAR(100)             NOT NULL,
    type        VARCHAR(100)             NOT NULL,
    description TEXT                     NULL,
    created_on  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on  TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (tenant_id, name, type),
    UNIQUE (id)
);

CREATE TABLE IF NOT EXISTS tenant.authorization_role_permission
(
    role_id       INT4                     NOT NULL REFERENCES tenant.authorization_role (id) ON DELETE CASCADE,
    permission_id INT4                     NOT NULL REFERENCES tenant.authorization_permission (id) ON DELETE CASCADE,
    created_on    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on    TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS tenant.authorization_role_user
(
    role_id     INT4                     NOT NULL REFERENCES tenant.authorization_role (id) ON DELETE CASCADE,
    user_id     INT4                     NOT NULL REFERENCES tenant.user (id) ON DELETE CASCADE,
    description TEXT                     NULL,
    created_on  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_on  TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (role_id, user_id)
);

