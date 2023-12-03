DO
'
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT table_name FROM information_schema.tables WHERE table_schema = ''internal'' AND table_name != ''configuration'')
    LOOP
        EXECUTE ''TRUNCATE TABLE internal.'' || quote_ident(r.table_name) || '' CASCADE'';
    END LOOP;
END;
';