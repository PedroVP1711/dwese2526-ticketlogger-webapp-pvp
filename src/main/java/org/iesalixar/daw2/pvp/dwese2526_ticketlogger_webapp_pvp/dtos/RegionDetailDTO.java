package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDetailDTO {
    private Long id;
    private String code;
    private String name;
    private List<ProvinceDTO> provinces;


}
