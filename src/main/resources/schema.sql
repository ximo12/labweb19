

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


