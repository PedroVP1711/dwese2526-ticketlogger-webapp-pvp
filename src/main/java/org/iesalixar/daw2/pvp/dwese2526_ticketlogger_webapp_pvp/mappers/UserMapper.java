package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Role;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    // ----------------------------
    // Entity -> DTO
    // ----------------------------

    public static UserDTO toDTO(User entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.getActive());
        dto.setAccountNonLocked(entity.getAccountNonLocked());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.getEmailVerified());
        dto.setMustChangePassword(entity.getMustChangePassword());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiredAt(entity.getPasswordExpiredAt());

        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            Set<String> roleNames = entity.getRoles().stream()
                    .map(Role::getDisplayName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }

    public static List<UserDTO> toDTOList(List<User> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(UserMapper::toDTO).toList();
    }

    public static UserUpdateDTO toUpdateDTO(User entity) {
        if (entity == null) return null;
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.getActive());
        dto.setAccountNonLocked(entity.getAccountNonLocked());
        dto.setEmailVerified(entity.getEmailVerified());
        dto.setMustChangePassword(entity.getMustChangePassword());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());

        if (entity.getRoles() != null) {
            Set<Long> roleIds= entity.getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());
            dto.setRoleIds(roleIds);
        }

        return dto;
    }

    public static UserDetailDTO toDetailDTO(User entity) {
        if (entity == null) return null;
        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.getActive());
        dto.setAccountNonLocked(entity.getAccountNonLocked());
        dto.setFailedLoginAttempts(entity.getFailedLoginAttempts());
        dto.setEmailVerified(entity.getEmailVerified());
        dto.setMustChangePassword(entity.getMustChangePassword());
        dto.setLastPasswordChange(entity.getLastPasswordChange());
        dto.setPasswordExpiredAt(entity.getPasswordExpiredAt());

        UserProfile profile = entity.getProfile();

        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }

        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            Set<String> roleNames = entity.getRoles().stream()
                    .map(Role::getDisplayName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }

    // ----------------------------
    // DTO -> Entity
    // ----------------------------

    public static User toEntity(UserCreateDTO dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPassword()); // hash en service antes de guardar
        entity.setActive(dto.isActive());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setEmailVerified(dto.getEmailVerified());
        entity.setMustChangePassword(dto.isMustChangePassword());
        return entity;
    }

    public static User toEntity(UserUpdateDTO dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setActive(dto.isActive());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setEmailVerified(dto.getEmailVerified());
        entity.setMustChangePassword(dto.isMustChangePassword());
        entity.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        // La contraseña se debe manejar aparte si se quiere actualizar
        return entity;
    }

    public static void copyToExistingEntity(UserUpdateDTO dto, User entity) {
        if (dto == null || entity == null) return;
        entity.setEmail(dto.getEmail());
        entity.setActive(dto.isActive());
        entity.setAccountNonLocked(dto.isAccountNonLocked());
        entity.setEmailVerified(dto.getEmailVerified());
        entity.setMustChangePassword(dto.isMustChangePassword());
        entity.setFailedLoginAttempts(dto.getFailedLoginAttempts());
        // No tocar id ni password aquí
    }

    public static User toEntity(UserCreateDTO dto, Set<Role> roles) {
        if (dto == null) return null;

        User e = toEntity(dto); // reutilizamos la lógica existente
        e.setRoles(roles);
        return e;
    }


    public static User toEntity(UserUpdateDTO dto, Set<Role> roles) {
        if (dto == null) return null;

        User e = toEntity(dto); // reutilizamos la lógica existente
        e.setRoles(roles);
        return e;
    }

}