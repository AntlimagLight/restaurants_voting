DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE restaurants
(
    id    INTEGER DEFAULT nextval('GLOBAL_SEQ') PRIMARY KEY,
    name  VARCHAR(255)      NOT NULL,
    CONSTRAINT restaurants UNIQUE (name)
);

CREATE TABLE users
(
    id          INTEGER   DEFAULT nextval('GLOBAL_SEQ') PRIMARY KEY,
    name        VARCHAR(255)            NOT NULL,
    email       VARCHAR(255)            NOT NULL,
    password    VARCHAR(255)            NOT NULL,
    registered  TIMESTAMP DEFAULT now() NOT NULL,
    enabled     BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

CREATE TABLE user_roles
(
    user_id INTEGER      NOT NULL,
    role    VARCHAR(255) NOT NULL,
    CONSTRAINT user_roles UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
    id            INTEGER DEFAULT nextval('GLOBAL_SEQ') PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    meal_date     TIMESTAMP DEFAULT now() NOT NULL,
    cost          INTEGER      NOT NULL,
    restaurant_id INTEGER      NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);
CREATE INDEX meals_restaurant_idx
    ON meals (restaurant_id);

CREATE TABLE votes
(
    id            LONG AUTO_INCREMENT PRIMARY KEY,
    vote_date     TIMESTAMP DEFAULT now() NOT NULL,
    user_id       INTEGER                 NOT NULL,
    restaurant_id INTEGER                 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);
