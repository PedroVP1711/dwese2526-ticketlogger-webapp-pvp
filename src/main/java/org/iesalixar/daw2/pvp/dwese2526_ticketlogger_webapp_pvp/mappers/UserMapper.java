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
   return new UserDTO(entity.getId(), entity.getEmail(), entity.getEmail());
    }

    public static List<UserDTO> toDTOList(List<User> entities) {
        return entities.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    public static UserDetailDTO toDetailDTO(User entity) {
        if (entity == null) return null;

        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setActive(entity.getActive());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.getEmailVerified());
        dto.setMustChangePassword(entity.getMustChangePassword());

        UserProfile profile = entity.getProfile();

        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }
        return dto;
    }
}