
DROP TABLE if EXISTS user_web;
CREATE TABLE user_web
(
  username VARCHAR (55) PRIMARY KEY,
  name VARCHAR (255),
  second_name VARCHAR (255),
  email VARCHAR (100),
  password VARCHAR(70) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1
);

DROP TABLE if EXISTS authorities;
CREATE TABLE authorities (
  authority_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (authority_id),
  UNIQUE KEY uni_username_role (role,username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES user_web (username));
