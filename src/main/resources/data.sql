INSERT INTO user_web (username, name, surname, mail, phone, password, date_create, date_edit, date_birth) VALUES ('quimmo', 'Quim', 'Molina', 'test@test.es', '123456789', '{noop}quim', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO user_roles (username, role) VALUES ('quimmo', 'ROLE_USER');
