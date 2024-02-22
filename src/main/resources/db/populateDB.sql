TRUNCATE users CASCADE;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2024-02-21 08:05:00', 'Завтрак', 1000),
       (100000, '2024-02-21 13:30:06', 'Обед', 1500),
       (100000, '2024-02-21 18:10:30', 'Ужин', 1000),
       (100000, '2024-02-22 08:10:30', 'Завтрак', 600),
       (100000, '2024-02-22 14:25:30', 'Обед', 900),
       (100000, '2024-02-22 19:00:00', 'Ужин', 450),
       (100001, '2024-02-10 08:05:00', 'Завтрак', 3000),
       (100001, '2024-02-10 13:30:06', 'Обед', 1500),
       (100001, '2024-02-10 18:10:30', 'Ужин', 1000),
       (100001, '2024-02-15 08:10:30', 'Завтрак', 750),
       (100001, '2024-02-15 14:25:30', 'Обед', 900),
       (100001, '2024-02-15 19:00:00', 'Ужин', 250);
