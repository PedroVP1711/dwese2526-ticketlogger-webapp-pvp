package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private Long id; // Necesario para saber qué usuario actualizar

    @NotEmpty(message = "{msg.users.email.notEmpty}")
    private String email;

    // La contraseña puede ser opcional al actualizar
    private String password;

    private boolean active;
    private boolean accountNonLocked;

    @NotNull(message = "{msg.users.emailVerified.notNull}")
    private Boolean emailVerified;

    private boolean mustChangePassword;

    // Opcional: incluir si quieres permitir actualización de intentos fallidos
    private int failedLoginAttempts;

    @NotEmpty(message = "{msg.user.roles.notempty}")
    private Set<Long> roleIds = new HashSet<>();
}