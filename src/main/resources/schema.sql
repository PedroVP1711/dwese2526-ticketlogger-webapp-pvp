-- Crear tabla para las Comunidades Autónomas de España
CREATE TABLE IF NOT EXISTS regions (
   id INT AUTO_INCREMENT PRIMARY KEY,
   code VARCHAR(10) NOT NULL UNIQUE,
   name VARCHAR(100) NOT NULL
);

-- 1. Asegúrate de que esta línea esté al inicio.
DROP TABLE IF EXISTS users;

-- 2. La sentencia CREATE TABLE ahora será la segunda sentencia (#2)
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    last_password_change TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password_expires_at TIMESTAMP NULL,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE
);