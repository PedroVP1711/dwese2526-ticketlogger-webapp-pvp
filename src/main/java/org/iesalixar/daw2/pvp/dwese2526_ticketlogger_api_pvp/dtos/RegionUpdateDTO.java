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
public class RegionUpdateDTO {
    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 5)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;
}