package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        // Usamos el constructor generado por Lombok (@AllArgsConstructor en UserDTO)
        return new UserDTO(entity.getId(), entity.getUsername(), entity.getEmail());
    }

    public static UserDetailDTO toDetailDTO(User entity) {
        if (entity == null) {
            return null;
        }
        // Usamos el constructor generado por Lombok (@AllArgsConstructor en UserDetailDTO)
        return new UserDetailDTO(entity.getId(), entity.getUsername(), entity.getEmail());
    }

    public static List<UserDTO> toDTOList(List<User> entities) {
        return entities.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}