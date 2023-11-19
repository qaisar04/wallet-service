CREATE SCHEMA wallet;


CREATE TABLE wallet.audits (
                               id SERIAL PRIMARY KEY,
                               player_username VARCHAR(255) NOT NULL,
                               audit_type VARCHAR(50) NOT NULL,
                               action_type VARCHAR(50) NOT NULL
);

CREATE TABLE wallet.players (
                                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                username VARCHAR(255) NOT NULL,
                                password VARCHAR(255) NOT NULL,
                                role VARCHAR(32),
                                balance NUMERIC(10, 2) DEFAULT 0.0
);

CREATE TABLE wallet.transactions (
                                     transaction_id UUID PRIMARY KEY,
                                     type VARCHAR(255) NOT NULL,
                                     amount NUMERIC(10, 2) NOT NULL,
                                     balance_before NUMERIC(10, 2),
                                     balance_after NUMERIC(10, 2),
                                     player_id BIGINT,
                                     FOREIGN KEY (player_id) REFERENCES wallet.players(id)
);


drop table wallet.players;
drop table wallet.transactions;
drop table wallet.players;


