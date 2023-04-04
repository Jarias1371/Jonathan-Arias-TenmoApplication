BEGIN TRANSACTION;

DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS tenmo_user;

DROP SEQUENCE IF EXISTS seq_user_id;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) UNIQUE NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(20),
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE TABLE accounts (
	id SERIAL PRIMARY KEY,
	user_id INTEGER UNIQUE NOT NULL,
	balance DECIMAL(10,2) DEFAULT 1000.00,
	CONSTRAINT FK_accounts_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT CHK_balance CHECK(balance >= 0)
);

CREATE TABLE requests (
	id SERIAL PRIMARY KEY,
	paying_user_id INTEGER NOT NULL,
	receiving_user_id INTEGER NOT NULL,
	request_status varchar(15) default 'Pending',
	transfer_amount varchar(20) NOT NULL,
	request_time TIMESTAMP default CURRENT_TIMESTAMP,
	FOREIGN KEY (paying_user_id) REFERENCES accounts(id),
	FOREIGN KEY (receiving_user_id) REFERENCES accounts(id)
);

CREATE TABLE transactions (
	id SERIAL PRIMARY KEY,
	paying_user_id INTEGER NOT NULL,
	receiving_user_id INTEGER NOT NULL,
	transfer_amount DECIMAL (10,2) NOT NULL,
	transaction_time TIMESTAMP default CURRENT_TIMESTAMP,
	status varchar(15) DEFAULT 'Pending',
	FOREIGN KEY (paying_user_id) REFERENCES accounts(user_id),
	FOREIGN KEY (receiving_user_id) REFERENCES accounts(user_id),
	CONSTRAINT CHK_transfer_amount CHECK(transfer_amount > 0)
);

INSERT INTO tenmo_user(user_id, username, password_hash)
VALUES(1001, 'andrew', '$2a$10$jKxF6iNs1IwAHXiiWuaHtu81jSZ7TEalmTFqW2kHj.d9gv3kNKwT6'),
(1002, 'jonathan', '$2a$10$PVvRag3I0XZIOwTruwWDJe5VSDKBB.t1Wwhg9rzGaVhqi5WWhUD1u'),
(1003, 'evan', '$2a$10$rWpBu6thFnNvyHc/jz8QAOu3ve001yOQ/14Gn5Fj8eH4BsmZ7HIxa');

INSERT INTO accounts(user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'andrew')),
((SELECT user_id FROM tenmo_user WHERE username = 'jonathan')),
((SELECT user_id FROM tenmo_user WHERE username = 'evan'));

COMMIT;