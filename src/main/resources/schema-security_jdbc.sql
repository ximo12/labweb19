DROP TABLE if EXISTS user_web;
CREATE TABLE user_web
(
  id bigint auto_increment PRIMARY KEY,
  username VARCHAR(55) UNIQUE,
  name VARCHAR (55),
  surname VARCHAR (55),
  mail VARCHAR (100),
  phone VARCHAR (15),
  password VARCHAR(150),
  date_create TIMESTAMP,
  date_edit TIMESTAMP,
  date_birth TIMESTAMP,
);


DROP TABLE if EXISTS user_roles;
CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  CONSTRAINT fk_username_role FOREIGN KEY (username) REFERENCES user_web (username));

