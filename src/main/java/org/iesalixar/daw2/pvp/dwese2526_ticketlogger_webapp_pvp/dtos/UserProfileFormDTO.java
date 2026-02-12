package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserProfileFormDTO {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
    private String locale;

    // Imagen
    private MultipartFile profileImageFile;

    // Para mostrar la imagen actual en el formulario (si existe)
    private String profileImage;

}
