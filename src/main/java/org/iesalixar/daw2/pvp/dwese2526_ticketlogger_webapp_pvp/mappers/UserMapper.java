package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        if (entity == null) return null;
        // CORRECCIÓN: Usar getEmail() en lugar de getUsername()
        // Asumo que UserDTO tiene el constructor (Long id, String username, String email)
        // El campo 'username' en el DTO será rellenado con el valor del 'email' de la entidad.
        return new UserDTO(entity.getId(), entity.getEmail(), entity.getEmail()); // <-- Corregido (antes usaba getUsername())
    }

    public static List<UserDTO> toDTOList(List<User> entities) {
        return entities.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public static UserDetailDTO toDetailDTO(User entity) {
        if (entity == null) {
            return null;
        }

        UserProfile profile = entity.getUserProfile();

        return UserDetailDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                // CORRECCIÓN: Usar getEmail() en lugar de getUsername()
                .username(entity.getEmail()) // <-- Corregido (antes usaba getUsername())

                // Extracción segura de datos de perfil
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .phoneNumber(profile != null ? profile.getPhoneNumber() : null)
                .profileImage(profile != null ? profile.getProfileImagePath() : null)
                .bio(profile != null ? profile.getBio() : null)
                .locale(profile != null ? profile.getLocale() : null)
                .build();
    }
}