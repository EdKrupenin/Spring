CREATE TABLE app_user (
    id       BIGSERIAL PRIMARY KEY,
    login    TEXT NOT NULL,
    password TEXT NOT NULL
);