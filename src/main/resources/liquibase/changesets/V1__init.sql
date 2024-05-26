CREATE TABLE IF NOT EXISTS account
(
    id              BIGSERIAL PRIMARY KEY,
    version         BIGINT         NOT NULL,
    initial_deposit NUMERIC(10, 2) NOT NULL CHECK ( initial_deposit >= 0 ),
    balance         NUMERIC(10, 2) NOT NULL CHECK ( balance >= 0 )
);

CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(128)                   NOT NULL UNIQUE,
    password   VARCHAR(128)                   NOT NULL,
    full_name  VARCHAR(255)                   NOT NULL,
    birth_date DATE                           NOT NULL,
    account_id BIGINT REFERENCES account (id) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS email
(
    id      BIGSERIAL PRIMARY KEY,
    address VARCHAR(128) NOT NULL UNIQUE,
    user_id BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS phone
(
    id      BIGSERIAL PRIMARY KEY,
    number  VARCHAR(32) NOT NULL UNIQUE,
    user_id BIGINT REFERENCES users (id)
);