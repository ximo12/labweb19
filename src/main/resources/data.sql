INSERT INTO user_web (username, name, surname, mail, phone, password, date_create, date_edit, date_birth) VALUES ('quimmo', 'Quim', 'Molina', 'test@test.es', '123456789', '{noop}quim', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_roles (username, role) VALUES ('quimmo', 'ROLE_USER');

INSERT INTO post_web (title, text, is_public, date_create, date_edit, owner) VALUES ('test', 'test text', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'quimmo');