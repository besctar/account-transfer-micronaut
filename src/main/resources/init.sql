
--DROP TABLE debit_account;

CREATE TABLE IF NOT EXISTS debit_account (
    id IDENTITY NOT NULL PRIMARY KEY,
    balance DECIMAL(25,5) NOT NULL
);