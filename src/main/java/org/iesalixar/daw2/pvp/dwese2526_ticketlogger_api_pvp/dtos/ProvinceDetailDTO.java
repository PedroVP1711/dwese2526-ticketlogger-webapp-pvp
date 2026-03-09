package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProvinceDetailDTO {
    private Long id;
    private String code;
    private String name;

    private Long regionId;
    private String regionName;

    public void setRegion(RegionDTO dto) {
    }
}
