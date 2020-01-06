#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE TABLE users (\
    id BIGSERIAL PRIMARY KEY,\
    status VARCHAR(100) CHECK ( status in ('ACTIVE', 'DELETED', 'BANNED') ) DEFAULT 'ACTIVE',\
    username VARCHAR(100) NOT NULL,\
    password VARCHAR(255) NOT NULL,\
    first_name VARCHAR(100),\
    last_name VARCHAR(100)\
  );

  CREATE TABLE roles (\
    id BIGSERIAL PRIMARY KEY,\
    status VARCHAR(100) CHECK ( status in ('ACTIVE', 'DELETED', 'BANNED') ) DEFAULT 'ACTIVE',\
    name VARCHAR(100)\
  );

  CREATE TABLE user_roles (\
    id BIGSERIAL PRIMARY KEY,\
    user_id BIGINT REFERENCES users (id)  ON UPDATE CASCADE ON DELETE CASCADE,\
    role_id BIGINT  REFERENCES roles (id)  ON UPDATE CASCADE\
  );

  INSERT INTO roles (name) VALUES \
    ('ROLE_ADMIN'),	\
    ('ROLE_USER');
EOSQL