package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileFormDTO {

    private Long userId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    private String phoneNumber;
    private String profileImagePath;
    private String bio;
    private String locale;

    // Campos para posible cambio de contraseña
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    // CORRECCIÓN: El método que causa el error "missing return statement" (LÍNEA 57 aprox.)
    public boolean hasPasswordChange() {
        return (this.newPassword != null && !this.newPassword.isBlank());
    }
}