package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.mappers;


import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.ProvinceDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.ProvinceDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.ProvinceUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Province;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Region;

import java.util.List;

/**
 * Mapper utilitario entre la entidad Province y sus DTOS.
 */
public class ProvinceMapper {

    // ----------------------------------
    // Entity DTO -> (listado/tabla básico)
    // ----------------------------------

    public static ProvinceDTO toDTO(Province entity) {
        if (entity == null) return null;
        ProvinceDTO dto = new ProvinceDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setRegionName(entity.getRegion() != null ? entity.getRegion().getName() : null);
        return dto;
    }

    public static List<ProvinceDTO> toDTOList(List<Province> entities) {
        if (entities == null) return List.of();
        return entities.stream().map (ProvinceMapper::toDTO).toList();
    }

    // ______________________________
    // Entity -> DTO (detalle con región)
    // ______________________________

    public static ProvinceDetailDTO toDetailDTO(Province entity) {
        if (entity == null) return null;
        ProvinceDetailDTO dto = new ProvinceDetailDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());

        // ⭐ Corregido: Usar el nombre de la región para el DTO de detalle
        dto.setRegionName(entity.getRegion() != null ? entity.getRegion().getName() : "No disponible");

        return dto;
    }

    public static ProvinceUpdateDTO toUpdateDTO(Province entity) {
        if (entity == null) return null;
        ProvinceUpdateDTO dto = new ProvinceUpdateDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setRegionId(entity.getRegion() != null ? entity.getRegion().getId() : null);
        return dto;
    }

    // ______________________________
    // DTO (create/update) -> Entity
    // ______________________________

    /**
     * Crea una nueva entidad {@link Province} desde un {@link ProvinceCreateDTO},
     * asignando la entidad Region ya cargada (la buscamos en el Controller).
     */
    public static Province toEntity(ProvinceCreateDTO dto, Region regionEntity) { // ⭐ Método de CREACIÓN
        if (dto == null) return null;
        Province e = new Province();
        e.setCode(dto.getCode());
        e.setName(dto.getName());
        e.setRegion(regionEntity);
        return e;
    }

    /**
     * Crea una entidad {@link Province} desde un {@link ProvinceUpdateDTO},
     * asignando la entidad Region ya cargada (la buscamos en el Controller).
     */
    public static Province toEntity(ProvinceUpdateDTO dto, Region regionEntity) { // ⭐ Método de ACTUALIZACIÓN
        if (dto == null) return null;
        Province e = new Province();
        e.setId(dto.getId());
        e.setCode(dto.getCode());
        e.setName(dto.getName());
        e.setRegion(regionEntity);
        return e;
    }

    /**
     * Copia campos editables desde un {@link ProvinceUpdateDTO} a una entidad existente.
     */
    public static void copyToExistingEntity(ProvinceUpdateDTO dto, Province entity, Region regionEntity) {
        if (dto == null || entity == null) return;
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setRegion(regionEntity);
    }
}