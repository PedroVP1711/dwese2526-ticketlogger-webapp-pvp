package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    // *** CORRECCIÓN DE MAPPING: AÑADIR @Column(name="...") ***

    @Column(name = "active")
    private boolean enabled = true;

    @Column(name = "account_non_locked") // <--- Usando el nombre de la Captura 1
    private boolean accountNonLocked = true;

    @Column(name = "email_verified") // <--- Usando el nombre de la Captura 1
    private boolean emailVerified = false;
    // Relación 1:1 con UserProfile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

}