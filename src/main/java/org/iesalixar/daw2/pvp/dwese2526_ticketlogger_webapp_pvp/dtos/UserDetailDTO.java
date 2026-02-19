package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {

    private Long id;
    private String email;
    private String PasswordHash;
    private boolean Active;
    private Boolean accountNonLocked;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiredAt;
    private Integer failedLoginAttempts = 0;
    private Boolean emailVerified = Boolean.FALSE;
    private Boolean mustChangePassword = Boolean.FALSE;


    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImage;
    private String bio;
    private String locale;

    private Set<String> roles;
}