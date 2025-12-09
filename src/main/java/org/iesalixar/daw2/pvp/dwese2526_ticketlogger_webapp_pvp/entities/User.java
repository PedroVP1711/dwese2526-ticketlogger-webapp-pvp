package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant; // Usamos Instant para mapear TIMESTAMP de SQL

@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera constructor sin argumentos
// NOTA: @AllArgsConstructor podría generar un constructor muy largo, pero lo mantenemos si lo necesitas.
public class User {

    private Long id;
    private String username;
    private String email;

    // CORRECCIÓN CLAVE: Debe coincidir con la columna 'password_hash' en la BD.
    private String passwordHash;

    // Campos de estado y seguridad (BOOLEAN en BD)
    private boolean active;
    private boolean accountNonLocked;

    // Campos de gestión de contraseñas (TIMESTAMP en BD)
    private Instant lastPasswordChange; // Mapea a TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    private Instant passwordExpiresAt;  // Mapea a TIMESTAMP NULL
    private boolean mustChangePassword;

    // Campos de seguridad (INT y BOOLEAN en BD)
    private int failedLoginAttempts;
    private boolean emailVerified;


    // =========================================================
    // Constructor para listado básico (si lo usas en el DAO)
    // Se deja vacío ya que @Data y @NoArgsConstructor/AllArgsConstructor se encargan,
    // pero si necesitas uno específico:
    // =========================================================

    public User(Long id, String username, String email, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;

        // Inicializar los campos booleanos/enteros que no se pasan en constructores simples
        this.active = true;
        this.accountNonLocked = true;
        this.failedLoginAttempts = 0;
        this.emailVerified = false;
        this.mustChangePassword = false;
    }

    // NOTA: Si @AllArgsConstructor te da problemas con el constructor que incluye todos los campos,
    // considera eliminarlo y crear un constructor manual o usar el patrón Builder.
}