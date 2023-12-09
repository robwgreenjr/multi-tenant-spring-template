INSERT INTO internal.user (first_name, last_name, email, phone)
VALUES ('User', 'Internal', 'user@internal.io', '111-111-5555')
ON CONFLICT DO NOTHING;

INSERT INTO internal.authorization_role_user (role_id, user_id)
VALUES ((SELECT id
         FROM internal.authorization_role
         WHERE name = 'TOP_LEVEL'),
        (SELECT id
         FROM internal.user
         WHERE email = 'user@internal.io'))
ON CONFLICT DO NOTHING;
