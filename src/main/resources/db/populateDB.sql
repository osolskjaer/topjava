DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories) VALUES
  (100000, '01.01.2016 12:00', 'apple', 100),
  (100000, '31.12.2015', 'banana', 200),
  (100000, '31.12.2015 07:00', 'cucumber', 20),
  (100001, '10.12.2015 16:15', 'dairy', 80),
  (100001, date(now()), 'eggs', 180),
  (100001, '31.12.2015 20:00', 'fry', 400),
  (100001, '30.12.2015 6:30', 'grapes', 222),
  (100001, '30.12.2015 16:45', 'honey', 300),
  (100001, '31.12.2015 06:30', 'ice-cream', 400)
