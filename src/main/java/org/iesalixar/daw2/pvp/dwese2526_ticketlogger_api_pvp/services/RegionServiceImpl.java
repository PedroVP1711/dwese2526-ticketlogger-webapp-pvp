package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Region;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.mappers.RegionMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories.regionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final regionRepository regionRepository;

    public RegionServiceImpl(regionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    // ==============================
    // GET ALL REGIONS
    // ==============================
    @Override
    public List<RegionDTO> getAllRegions() {
        logger.info("Recuperando todas las regiones...");
        List<Region> regions = regionRepository.findAll();
        return regions.stream()
                .map(RegionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ==============================
    // GET REGION BY ID
    // ==============================
    @Override
    public RegionDTO getRegionById(Long id) {
        logger.info("Buscando región con ID {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("region", "id", id));
        return RegionMapper.toDTO(region);
    }

    // ==============================
    // CREATE REGION
    // ==============================
    @Override
    public RegionDTO createRegion(RegionCreateDTO dto) {
        logger.info("Creando región con código {}", dto.getCode());

        if (regionRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("region", "code", dto.getCode());
        }

        Region region = RegionMapper.toEntity(dto);
        region = regionRepository.save(region);

        logger.info("Región creada con ID {}", region.getId());
        return RegionMapper.toDTO(region);
    }

    @Override
    public RegionDTO updateRegion(Long id, RegionCreateDTO dto, Locale locale) {
        return null;
    }

    @Override
    public RegionDTO updateRegion(Long id, RegionUpdateDTO dto) {
        return null;
    }

    // ==============================
    // UPDATE REGION
    // ==============================
    @Override
    public RegionDTO updateRegion(Long id, RegionUpdateDTO dto, Locale locale) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("region", "id", id));

        // Copiar campos del DTO de actualización a la entidad
        RegionMapper.copyToExistingEntity(dto, region);

        region = regionRepository.save(region);

        logger.info("Región con ID {} actualizada.", id);
        return RegionMapper.toDTO(region);
    }

    // ==============================
    // DELETE REGION
    // ==============================
    @Override
    public boolean deleteRegion(Long id) {
        logger.info("Eliminando región con ID {}", id);

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("region", "id", id));

        regionRepository.delete(region);
        logger.info("Región con ID {} eliminada.", id);
        return true;
    }
}