package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.RegionUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // ============================================
    // LISTADO DE REGIONES
    // ============================================
    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        try {
            List<RegionDTO> regions = regionService.getAllRegions();
            return ResponseEntity.ok(regions);
        } catch (Exception e) {
            logger.error("Error al listar regiones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============================================
    // DETALLE POR ID
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        try {
            RegionDTO dto = regionService.getRegionById(id);
            if (dto == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error al buscar región {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============================================
    // CREAR NUEVA REGION
    // ============================================
    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionCreateDTO dto) {
        try {
            RegionDTO created = regionService.createRegion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error al crear región: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============================================
    // ACTUALIZAR REGION
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionUpdateDTO dto) {

        try {
            RegionDTO updated = regionService.updateRegion(id, dto);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ============================================
    // ELIMINAR REGION
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        try {
            boolean deleted = regionService.deleteRegion(id);
            if (!deleted) return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar región {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}