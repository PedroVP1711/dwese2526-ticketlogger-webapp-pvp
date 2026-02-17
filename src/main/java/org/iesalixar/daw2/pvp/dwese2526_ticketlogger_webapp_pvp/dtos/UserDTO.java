package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder; // <-- IMPORTANTE

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder // <-- Soluciona el error 'cannot find symbol UserDTOBuilder'
public class UserDTO {
    private Long id;
    private String email;
    private String username;
}