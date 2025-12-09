-- Inserts de las Comunidades Autónomas, ignora si se produce un error en la insercción
INSERT IGNORE INTO regions (id, code, name) VALUES
(1, '01', 'ANDALUCÍA'),
(2, '02', 'ARAGÓN'),
(3, '03', 'ASTURIAS'),
(4, '04', 'BALEARES'),
(5, '05', 'CANARIAS'),
(6, '06', 'CANTABRIA'),
(7, '07', 'CASTILLA Y LEÓN'),
(8, '08', 'CASTILLA-LA MANCHA'),
(9, '09', 'CATALUÑA'),
(10, '10', 'COMUNIDAD VALENCIANA'),
(11, '11', 'EXTREMADURA'),
(12, '12', 'GALICIA'),
(13, '13', 'MADRID'),
(14, '14', 'MURCIA'),
(15, '15', 'NAVARRA'),
(16, '16', 'PAÍS VASCO'),
(17, '17', 'LA RIOJA'),
(18, '18', 'CEUTA Y MELILLA');
-- data.sql (Script Corregido)

-- Inserta si no existe (ignora duplicados por UNIQUE username o id)
INSERT IGNORE INTO users (
    id, username, email, password_hash, active, account_non_locked,
    last_password_change, password_expires_at, failed_login_attempts,
    email_verified, must_change_password
) VALUES
(1, 'admin', 'admin@ticketlogger.com', 'admin123',  TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE,  FALSE),
(2, 'jdoe', 'jdoe@ticketlogger.com', '1234',      TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 1, FALSE, FALSE),
(3, 'maria', 'maria@ticketlogger.com', 'changeme',  TRUE,  TRUE,  NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE,  TRUE ),
(4, 'blockeduser', 'blocked@ticketlogger.com', 'secret',    FALSE, FALSE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 5, FALSE, FALSE);

-- Es fundamental terminar la sentencia con punto y coma (;)
-- si el script contiene una sola sentencia, aunque se recomienda usarlo siempre.