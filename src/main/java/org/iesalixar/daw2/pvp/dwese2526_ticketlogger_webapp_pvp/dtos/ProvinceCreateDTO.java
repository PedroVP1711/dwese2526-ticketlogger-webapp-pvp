package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceCreateDTO {

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