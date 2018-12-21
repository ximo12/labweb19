

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
  post_user_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  post_id varchar(45) NOT NULL,
  PRIMARY KEY (post_user_id),
  UNIQUE KEY uni_username_post (post_id,username),
  CONSTRAINT fk_username_shared FOREIGN KEY (username) REFERENCES user_web (username),
  CONSTRAINT fk_post FOREIGN KEY (post_id) REFERENCES post_web (id),
  );

