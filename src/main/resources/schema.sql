-- *** 1. DESHABILITAR LA RESTRICCIÓN DE CLAVES FORÁNEAS (OPCIONAL, PERO RECOMENDADO PARA DROPS) ***
SET FOREIGN_KEY_CHECKS = 0;

-- *** 2. DROP TABLES: Orden de eliminación (Hija -> Padre) ***

-- Elimina primero las tablas hijas (las que contienen las FKs)
DROP TABLE IF EXISTS user_profiles;
-- Si existiera una tabla 'tickets' o 'roles' que referencia a 'users', iría aquí
-- DROP TABLE IF EXISTS tickets;
-- DROP TABLE IF EXISTS user_roles;

-- Elimina la tabla users (la tabla padre) en último lugar
DROP TABLE IF EXISTS users;

-- Tablas sin dependencias
DROP TABLE IF EXISTS regions;

-- *** 3. REHABILITAR LA RESTRICCIÓN DE CLAVES FORÁNEAS ***
SET FOREIGN_KEY_CHECKS = 1;


-- *** 4. CREATE TABLES: Orden de creación (Padre -> Hija) ***

-- Crea la tabla padre primero
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    last_password_change DATETIME NULL,
    password_expires_at DATETIME NULL,
    failed_login_attempts INT DEFAULT 0,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE
    );

-- Crea la tabla hija después de que el padre exista
CREATE TABLE IF NOT EXISTS user_profiles (
                                             user_id BIGINT NOT NULL,
                                             first_name      VARCHAR(60)  NOT NULL,
    last_name       VARCHAR(80)  NOT NULL,
    phone_number    VARCHAR(30)  NULL,
    profile_image   VARCHAR(255) NULL,
    bio             VARCHAR(500) NULL,
    locale          VARCHAR(10)  NULL,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_profiles PRIMARY KEY (user_id),
    CONSTRAINT fk_user_profiles_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
                                                                ON DELETE CASCADE
                                                                ON UPDATE CASCADE
    );

-- Tabla sin dependencias
CREATE TABLE IF NOT EXISTS regions (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
    );

-- Suponiendo que aquí está el CREATE TABLE users...

-- ...

-- ------------------------------
-- AÑADIR COLUMNAS DE ESTADO FALTANTES
-- ------------------------------