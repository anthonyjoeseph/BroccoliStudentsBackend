create table USERS (
	USER_ID BIGINT not null AUTO_INCREMENT,
	NAME varchar(500) not null,
	PASSWORD varchar(500) not null,
	PRIMARY KEY (USER_ID)
);