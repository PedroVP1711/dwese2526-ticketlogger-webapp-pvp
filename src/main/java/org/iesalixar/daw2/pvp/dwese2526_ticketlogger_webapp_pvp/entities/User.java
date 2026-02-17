package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // 🔹 Relación 1–1 con UserProfile (lado NO propietario)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserProfile userProfile;

    // 🔹 Roles (muchos a muchos)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Password
    @Column(nullable = false)
    private String passwordHash;

    // Estado de la cuenta
    private boolean active = true;
    private boolean accountNonLocked = true;

    // Seguridad
    private int failedLoginAttempts = 0;
    private boolean emailVerified = false;
    private boolean mustChangePassword = false;

    // Gestión de contraseñas
    private Instant lastPasswordChange;
    private Instant passwordExpiresAt;

    // Constructor reducido
    public User(Long id, String username, String email, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // 🔹 Helper seguro para la imagen
    public String getProfileImage() {
        return userProfile != null ? userProfile.getProfileImage() : null;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }
}