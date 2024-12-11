CREATE TABLE IF NOT EXISTS account
(
    id              BIGSERIAL PRIMARY KEY,
    version         BIGINT         NOT NULL,
    initial_deposit NUMERIC(10, 2) NOT NULL CHECK ( initial_deposit >= 0 ),
    balance         NUMERIC(10, 2) NOT NULL CHECK ( balance >= 0 ),
    created_date    TIMESTAMP      NOT NULL,
    modified_date   TIMESTAMP,
    created_by      VARCHAR(128)   NOT NULL,
    modified_by     VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(128) NOT NULL UNIQUE,
    password      VARCHAR(128) NOT NULL,
    role          VARCHAR(32)  NOT NULL,
    created_date  TIMESTAMP    NOT NULL,
    modified_date TIMESTAMP,
    created_by    VARCHAR(128) NOT NULL,
    modified_by   VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS client
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT REFERENCES users (id)   NOT NULL UNIQUE,
    full_name     VARCHAR(255)                   NOT NULL,
    birth_date    DATE                           NOT NULL,
    account_id    BIGINT REFERENCES account (id) NOT NULL UNIQUE,
    created_date  TIMESTAMP                      NOT NULL,
    modified_date TIMESTAMP,
    created_by    VARCHAR(128)                   NOT NULL,
    modified_by   VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS email
(
    id            BIGSERIAL PRIMARY KEY,
    address       VARCHAR(128)                  NOT NULL UNIQUE,
    client_id     BIGINT REFERENCES client (id) NOT NULL,
    created_date  TIMESTAMP                     NOT NULL,
    modified_date TIMESTAMP,
    created_by    VARCHAR(128)                  NOT NULL,
    modified_by   VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS phone
(
    id            BIGSERIAL PRIMARY KEY,
    external_id   VARCHAR(36)                   NOT NULL UNIQUE,
    number        VARCHAR(32)                   NOT NULL UNIQUE,
    client_id     BIGINT REFERENCES client (id) NOT NULL,
    created_date  TIMESTAMP                     NOT NULL,
    modified_date TIMESTAMP,
    created_by    VARCHAR(128)                  NOT NULL,
    modified_by   VARCHAR(128)
);

CREATE TABLE IF NOT EXISTS revinfo
(
    id        BIGSERIAL PRIMARY KEY,
    timestamp BIGINT       NOT NULL,
    username  VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_aud
(
    id       BIGSERIAL,
    rev      BIGINT REFERENCES revinfo (id) NOT NULL,
    PRIMARY KEY (id, rev),
    revType  SMALLINT,
    username VARCHAR(128),
    role     VARCHAR(32)
);

