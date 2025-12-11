package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

public class UserProfileMapper {

    // CORRECCIÓN 1: Mapea la entidad UserProfile (no User) al DTO
    public static UserProfileFormDTO toFormDto(UserProfile entity) {
        if (entity == null) {
            return null;
        }

        UserProfileFormDTO dto = new UserProfileFormDTO();

        // Mapeo de UserProfile y sus campos de User a DTO
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setEmail(entity.getUser() != null ? entity.getUser().getEmail() : null);

        // *** CORRECCIÓN CRÍTICA (LÍNEA 19 ORIGINAL) ***
        // Se reemplaza getUsername() por getEmail()
        dto.setUsername(entity.getUser() != null ? entity.getUser().getEmail() : null); // <-- CORREGIDO

        // CORRECCIÓN 2: Uso de los métodos de UserProfile (los símbolos se encuentran)
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfileImagePath(entity.getProfileImagePath());
        dto.setBio(entity.getBio());
        dto.setLocale(entity.getLocale());

        return dto;
    }

    // CORRECCIÓN 3: Copia los datos del DTO a la entidad UserProfile existente (no User)
    public static void copyToExistingEntity(UserProfileFormDTO dto, UserProfile entity) {
        if (dto == null || entity == null) {
            return;
        }

        // Actualizar campos de UserProfile
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfileImagePath(dto.getProfileImagePath());
        entity.setBio(dto.getBio());
        entity.setLocale(dto.getLocale());

        // Nota: Los campos de User (email, username) deberían actualizarse usando userDAO.update(user)
        // después de este mapeo si se permite editarlos.
    }
}