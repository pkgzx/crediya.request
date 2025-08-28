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


CREATE TABLE IF NOT EXISTS "LoanApplication" (
    id VARCHAR(255) PRIMARY KEY,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL, -- ISO 4217 currency code
    term INTEGER NOT NULL, -- in months
    id_user VARCHAR(200) NOT NULL,
    id_state BIGINT NOT NULL,
    id_type_loan BIGINT NOT NULL,
--     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_state) REFERENCES "State"(id),
    FOREIGN KEY (id_type_loan) REFERENCES "TypeLoan"(id)
);

