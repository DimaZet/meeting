#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE TABLE users (\
    id BIGSERIAL PRIMARY KEY,\
    status VARCHAR(100),\
    created_at TIMESTAMPTZ,\
    updated_at TIMESTAMPTZ,\
    username VARCHAR(100),\
    password VARCHAR(255),\
    first_name VARCHAR(100),\
    last_name VARCHAR(100)\
  );

  CREATE TABLE roles (\
    id BIGSERIAL PRIMARY KEY,\
    status VARCHAR(100),\
    created_at TIMESTAMPTZ,\
    updated_at TIMESTAMPTZ,\
    name VARCHAR(100)\
  );

  CREATE TABLE user_roles (\
    id BIGSERIAL PRIMARY KEY,\
    user_id BIGINT REFERENCES users (id)  ON UPDATE CASCADE ON DELETE CASCADE,\
    role_id BIGINT  REFERENCES roles (id)  ON UPDATE CASCADE\
  );

  INSERT INTO roles (status, created_at, updated_at, name) VALUES \
    ('ACTIVE', current_timestamp, current_timestamp, 'ROLE_ADMIN'),	\
    ('ACTIVE', current_timestamp, current_timestamp, 'ROLE_USER');
EOSQL