package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionUpdateDTO;

import java.util.List;
import java.util.Locale;

public interface RegionService {

    /**
     * Devuelve todas las regiones como lista de DTOs
     */
    List<RegionDTO> getAllRegions();

    /**
     * Devuelve una región específica por su ID
     * @param id ID de la región
     * @return RegionDTO
     */
    RegionDTO getRegionById(Long id);

    /**
     * Crea una nueva región
     *
     * @param dto DTO con los datos de la región
     * @return RegionDTO de la región creada
     */
    RegionDTO createRegion(RegionCreateDTO dto);

    /**
     * Actualiza una región existente
     * @param id ID de la región a actualizar
     * @param dto DTO con los datos a actualizar
     * @param locale Idioma para mensajes (opcional)
     * @return RegionDTO de la región actualizada
     */
    RegionDTO updateRegion(Long id, RegionCreateDTO dto, Locale locale);

    RegionDTO updateRegion(Long id, RegionUpdateDTO dto);

    RegionDTO updateRegion(Long id, RegionUpdateDTO dto, Locale locale);

    /**
     * Elimina una región por su ID
     *
     * @param id ID de la región a eliminar
     * @return
     */
    boolean deleteRegion(Long id);

}

