DO
$$
    BEGIN
        -- Opcjonalnie ustaw poziom logów
        PERFORM set_config('client_min_messages', 'NOTICE', true);

        RAISE NOTICE 'app.mock_data: %', current_setting('app.mock_data', true);

        IF current_setting('app.mock_data', true) = 'true' THEN
            INSERT INTO public.portfolios_users (id, name, email, created_date, last_modified_date)
            VALUES (
                       '5f2d4a8d-3028-48a7-b389-e68ab9be020e',
                       'demo',
                       'dmeo@demo.com',
                       '2025-08-07 17:27:11.292743',
                       '2025-08-07 17:27:11.292743'
                   );
        END IF;
    END;
$$;