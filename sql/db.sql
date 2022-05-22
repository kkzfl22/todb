create table userinfo(
	id varchar(64) not null primary key,
	user_name varchar(64) not null,
	pwd varchar(128) not null,
	full_name varchar(128) not null,
	phone varchar(32) not null,
	email varchar(32) not null,
	card_id varchar(32) not null,
	address varchar(128) not null,
	city varchar(32) not null,
	create_time bigint(20) not null
)