CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       created_date TIMESTAMP NOT NULL DEFAULT NOW(),
                       last_modified_date TIMESTAMP DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION update_last_modified_date()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_last_modified_date
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_last_modified_date();