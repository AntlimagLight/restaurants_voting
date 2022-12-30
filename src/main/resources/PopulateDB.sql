-- noinspection SqlWithoutWhereForFile

DELETE
FROM restaurants;
DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Guest', 'guest@gmail.com', '{noop}guest'),
       ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100001),
       ('ADMIN', 100002),
       ('USER', 100002);

INSERT INTO restaurants (name)
VALUES ('Tasty Island'),
       ('Burger Master'),
       ('Sushi city');

INSERT INTO meals (name, cost, restaurant_id)
VALUES ('Soup', 50, 100003),
       ('Green salt', 100, 100003),
       ('Red Fish', 300, 100003),
       ('Hamburger', 100, 100004),
       ('Coca-cola', 150, 100004),
       ('Fry Potato', 120, 100004),
       ('Classic Sushi', 200, 100005),
       ('Wok', 180, 100005);
