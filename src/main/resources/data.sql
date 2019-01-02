INSERT INTO user_web (username, name, surname, mail, phone, password, date_create, date_edit, date_birth) VALUES ('quimmo', 'Quim', 'Molina', 'test@test.es', '123456789', '{noop}quim', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_roles (username, role) VALUES ('quimmo', 'ROLE_USER');

INSERT INTO user_web (username, name, surname, mail, phone, password, date_create, date_edit, date_birth) VALUES ('quimmo2', 'Quim', 'Molina', 'test2@test.es', '123456789', '{noop}quim', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_roles (username, role) VALUES ('quimmo2', 'ROLE_USER');

INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES ('test1', 'test text', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'quimmo');

INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES ('test2', 'test text', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'quimmo');

INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES ('test3', 'test text', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'quimmo');

INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES ('test4', 'test text', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'quimmo2');


INSERT INTO shared_post (username, post_id) VALUES ('quimmo2', 1);

INSERT INTO shared_post (username, post_id) VALUES ('quimmo', 2);

INSERT INTO shared_post (username, post_id) VALUES ('quimmo', 3);



