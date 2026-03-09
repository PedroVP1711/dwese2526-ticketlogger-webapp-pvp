package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String message;

}