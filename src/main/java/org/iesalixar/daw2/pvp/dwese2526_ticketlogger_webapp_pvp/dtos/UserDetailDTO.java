package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Asegura que equals/hashCode incluya los campos de UserDTO
public class UserDetailDTO extends UserDTO {

    // Hereda id, username, email de UserDTO gracias a @Data en UserDTO.

    // Si no hay campos extra, solo se necesita este constructor para el mapeo.
    public UserDetailDTO(Long id, String username, String email) {
        super(id, username, email);
    }
}