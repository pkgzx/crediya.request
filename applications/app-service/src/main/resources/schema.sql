CREATE TABLE IF NOT EXISTS "State" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);



CREATE TABLE IF NOT EXISTS "TypeLoan" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    min_amount NUMERIC(19, 2) NOT NULL,
    max_amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL, -- ISO 4217 currency code
    interest_rate DOUBLE PRECISION NOT NULL, -- example: 5.5 for 5.5%
    validation_automatic BOOLEAN NOT NULL
);


CREATE TABLE IF NOT EXISTS "Application" (
    id VARCHAR(255) PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL, -- ISO 4217 currency code
    term INTEGER NOT NULL, -- in months
    id_user VARCHAR(100) NOT NULL,
    state_id BIGINT NOT NULL,
    type_loan_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (state_id) REFERENCES "State"(id),
    FOREIGN KEY (type_loan_id) REFERENCES "TypeLoan"(id)
);

