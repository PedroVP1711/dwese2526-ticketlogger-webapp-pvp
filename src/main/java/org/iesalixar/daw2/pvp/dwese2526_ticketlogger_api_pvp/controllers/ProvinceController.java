package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.*;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Province;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Region;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.mappers.ProvinceMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories.provinceRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories.regionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private provinceRepository provinceDAO;

    @Autowired
    private regionRepository regionDAO;

    // ===========================================================
    // LISTADO PAGINADO
    // ===========================================================
    @GetMapping
    public ResponseEntity<Page<ProvinceDTO>> listProvinces(Pageable pageable) {
        Page<ProvinceDTO> page = provinceDAO.findAll(pageable).map(ProvinceMapper::toDTO);
        return ResponseEntity.ok(page);
    }

    // ===========================================================
    // DETALLE POR ID
    // ===========================================================
    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDTO> getProvinceById(@PathVariable Long id) {
        Optional<Province> provinceOpt = provinceDAO.findById(id);
        return provinceOpt
                .map(province -> ResponseEntity.ok(ProvinceMapper.toDTO(province)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===========================================================
    // CREAR NUEVA PROVINCIA
    // ===========================================================
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody ProvinceCreateDTO dto) {

        // 1️⃣ Validar que la región exista
        Optional<Region> regionOpt = regionDAO.findById(dto.getRegionId());
        if (regionOpt.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("La región con ID " + dto.getRegionId() + " no existe.");
        }

        // 2️⃣ Validar código único
        if (provinceDAO.existsByCode(dto.getCode())) {
            return ResponseEntity
                    .badRequest()
                    .body("Ya existe una provincia con el código: " + dto.getCode());
        }

        // 3️⃣ Crear y guardar la provincia
        Province province = new Province();
        province.setCode(dto.getCode());
        province.setName(dto.getName());
        province.setRegion(regionOpt.get());

        provinceDAO.save(province);

        // 4️⃣ Devolver DTO
        ProvinceDTO response = ProvinceMapper.toDTO(province);
        return ResponseEntity.ok(response);
    }

    // ===========================================================
    // ACTUALIZAR PROVINCIA
    // ===========================================================
// ===========================================================
// ACTUALIZAR PROVINCIA (PUT)
// ===========================================================
    @PutMapping("/{id}")
    public ResponseEntity<ProvinceDTO> updateProvince(@PathVariable Long id,
                                                      @Valid @RequestBody ProvinceUpdateDTO dto) {
        try {
            // 1. Buscar la provincia existente
            Province existingProvince = provinceDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id " + id));

            // 2. Buscar la región
            Region region = regionDAO.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Región no encontrada con id " + dto.getRegionId()));

            // 3. Copiar datos desde el DTO
            ProvinceMapper.copyToExistingEntity(dto, existingProvince, region);

            // 4. Guardar cambios
            provinceDAO.save(existingProvince);

            return ResponseEntity.ok(ProvinceMapper.toDTO(existingProvince));

        } catch (RuntimeException e) {
            logger.error("Error al actualizar provincia: {}", e.getMessage(), e);
            return ResponseEntity.status(400).body(null); // Bad Request con mensaje en logs
        } catch (Exception e) {
            logger.error("Error interno al procesar PUT /provinces: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }
    // ===========================================================
    // BORRAR PROVINCIA
    // ===========================================================
// ===========================================================
// BORRAR PROVINCIA (DELETE)
// ===========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        try {
            Province province = provinceDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Provincia no encontrada con id " + id));

            // Borra la entidad
            provinceDAO.delete(province);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            logger.error("Error al eliminar provincia: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error interno al procesar DELETE /provinces: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
    }