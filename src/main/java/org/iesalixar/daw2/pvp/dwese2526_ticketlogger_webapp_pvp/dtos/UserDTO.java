package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder; // <-- IMPORTANTE
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // <-- Soluciona el error 'cannot find symbol UserDTOBuilder'
public class UserDTO {
    private Long id;
    private String email;
    private String passwordHash;
    private UserProfile profile;
    private Boolean active;
    private Boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiredAt;
    private Integer failedLoginAttempts ;
    private Boolean emailVerified;
    private Boolean mustChangePassword;

    private Set<String> roles;
}