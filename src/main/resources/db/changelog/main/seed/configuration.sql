TRUNCATE TABLE internal.configuration CASCADE;

INSERT INTO internal.configuration (key, value)
VALUES ('JWT_SECRET', 'this value should be changed in prod')
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value)
VALUES ('JWT_EXPIRATION', '5555')
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value)
VALUES ('RESET_PASSWORD_EXPIRATION', '5555')
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value)
VALUES ('CREATE_PASSWORD_EXPIRATION', '5555')
ON CONFLICT DO NOTHING;
