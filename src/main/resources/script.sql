CREATE TABLE users (
	id int(11) NOT NULL AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	mobile varchar(20),
	email varchar(100),

	created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_date int(11) DEFAULT 0,
	 PRIMARY KEY (id)
);