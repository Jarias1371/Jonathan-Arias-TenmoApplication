--BEGIN TRANSACTION;
--
--DROP TABLE IF EXISTS tenmo_user;
--DROP SEQUENCE IF EXISTS seq_user_id;
--
--CREATE SEQUENCE seq_user_id
--  INCREMENT BY 1
--  START WITH 1001
--  NO MAXVALUE;
--
--CREATE TABLE tenmo_user (
--	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
--	username varchar(50) UNIQUE NOT NULL,
--	password_hash varchar(200) NOT NULL,
--	role varchar(20),
--	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
--	CONSTRAINT UQ_username UNIQUE (username)
--);
--INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user1','user1','ROLE_USER'); -- 1001
--INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user2','user2','ROLE_USER'); -- 1002
--INSERT INTO tenmo_user (username,password_hash,role) VALUES ('user3','user3','ROLE_USER');
--
--COMMIT TRANSACTION;


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
VALUES(1001, 'user1', 'user1'),
(1002, 'user2', 'user2'),
(1003, 'user3', 'user3');

INSERT INTO accounts(user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'user1')),
((SELECT user_id FROM tenmo_user WHERE username = 'user2')),
((SELECT user_id FROM tenmo_user WHERE username = 'user3'));

COMMIT TRANSACTION;


