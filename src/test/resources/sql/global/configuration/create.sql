TRUNCATE TABLE internal.configuration CASCADE;

INSERT INTO internal.configuration (key, value, hashed)
VALUES ('JWT_SECRET', 'this is a test', false)
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value, hashed)
VALUES ('JWT_EXPIRATION', '2400', false)
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value)
VALUES ('RESET_PASSWORD_EXPIRATION', '2400')
ON CONFLICT DO NOTHING;

INSERT INTO internal.configuration (key, value)
VALUES ('CREATE_PASSWORD_EXPIRATION', '2400')
ON CONFLICT DO NOTHING;