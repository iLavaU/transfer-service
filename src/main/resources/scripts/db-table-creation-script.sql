CREATE SEQUENCE IF NOT EXISTS accounts_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS owners_seq  START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS transfers_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE accounts
(
    id       BIGINT       NOT NULL,
    uuid     UUID         NOT NULL,
    owner_id UUID         NOT NULL,
    currency VARCHAR(255) NOT NULL,
    balance  DECIMAL      NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE TABLE owners
(
    id         BIGINT       NOT NULL,
    uuid       UUID         NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_owners PRIMARY KEY (id)
);

CREATE TABLE transfers
(
    id                   BIGINT                      NOT NULL,
    uuid                 UUID                        NOT NULL,
    recipient_account_id BIGINT                      NOT NULL,
    origin_account_id    BIGINT                      NOT NULL,
    transfer_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_transfers PRIMARY KEY (id)
);

ALTER TABLE accounts
    ADD CONSTRAINT uc_accounts_uuid UNIQUE (uuid);

ALTER TABLE owners
    ADD CONSTRAINT uc_owners_uuid UNIQUE (uuid);

ALTER TABLE transfers
    ADD CONSTRAINT uc_transfers_uuid UNIQUE (uuid);

ALTER TABLE accounts
    ADD CONSTRAINT FK_ACCOUNTS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES owners (uuid);

ALTER TABLE transfers
    ADD CONSTRAINT FK_TRANSFERS_ON_ORIGIN_ACCOUNT FOREIGN KEY (origin_account_id) REFERENCES accounts (id);

ALTER TABLE transfers
    ADD CONSTRAINT FK_TRANSFERS_ON_RECIPIENT_ACCOUNT FOREIGN KEY (recipient_account_id) REFERENCES accounts (id);