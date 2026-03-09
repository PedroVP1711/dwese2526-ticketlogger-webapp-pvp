package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceCreateDTO {

    private Long id;
    // El ID no se incluye en un DTO de creación.

    @NotBlank(message = "{msg.validation.notBlank}")
    @Size(max = 50, message = "{msg.validation.size}")
    private String code;

    @NotBlank(message = "{msg.validation.notBlank}")
    @Size(max = 255, message = "{msg.validation.size}")
    private String name;

    // ⭐ Campo CORRECTO que el Mapper estaba intentando usar
    @NotNull(message = "{msg.validation.notNull}")
    private Long regionId;

}