DO
'
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT table_name FROM information_schema.tables WHERE table_schema = ''internal'')
    LOOP
        EXECUTE ''TRUNCATE TABLE internal.'' || quote_ident(r.table_name) || '' CASCADE'';
    END LOOP;
END;
';

DO
'
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT table_name FROM information_schema.tables WHERE table_schema = ''tenant'')
    LOOP
        EXECUTE ''TRUNCATE TABLE tenant.'' || quote_ident(r.table_name) || '' CASCADE'';
    END LOOP;
END;
';