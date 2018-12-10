DROP TABLE if EXISTS user_web;
CREATE TABLE user_web
(
  id bigint auto_increment PRIMARY KEY,
  name VARCHAR (55),
  surname VARCHAR (55),
  mail VARCHAR (100),
  phone VARCHAR (15),
  password VARCHAR(150),
  date_create TIMESTAMP,
  date_edit TIMESTAMP,
  date_birth TIMESTAMP,
);

DROP TABLE if EXISTS post_web;
CREATE TABLE post_web (
  id bigint auto_increment PRIMARY KEY,
  title VARCHAR (100) ,
  text VARCHAR (255),
  date_create TIMESTAMP ,
  date_edit TIMESTAMP ,
  public BOOLEAN,
  owner VARCHAR (55),
  FOREIGN KEY (owner)
  REFERENCES user_web(mail)
);

DROP TABLE if EXISTS shared_post;
CREATE TABLE shared_post (
  id_user bigint auto_increment PRIMARY KEY,
  title VARCHAR (100) ,
  text VARCHAR (255),
  date_create TIMESTAMP ,
  date_edit TIMESTAMP ,
  public BOOLEAN,
  owner VARCHAR (55),
  FOREIGN KEY (owner)
  REFERENCES user_web(mail)
);


DROP TABLE if EXISTS user_roles;
CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,mail),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES user_web (mail));
