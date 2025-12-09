package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import java.util.List;
import java.util.Locale;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.ProvinceDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.RegionDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.*;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Province;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.ProvinceMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProvinceDAO provinceDAO;

    @Autowired
    private RegionDAO regionDAO;

    // ===========================================================
    // LISTADO
    // ===========================================================
    @GetMapping
    public String listProvinces(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model, Locale locale) {

        logger.info("Solicitando la lista de provincias... page={}, size={}, sortField={}, sortDir={}",
                page, size, sortField, sortDir);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        try {
            long totalElements = provinceDAO.countProvinces();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (totalPages > 0 && page >= totalPages) page = totalPages - 1;

            // NOTA: Debes adaptar tu DAO para que use sortField y sortDir
            List<Province> entities = provinceDAO.listProvincesPage(page, size);

            List<ProvinceDTO> dtos = ProvinceMapper.toDTOList(entities);

            model.addAttribute("provinces", dtos);

            // Atributos de paginación y ordenación
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);

        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage(), e);
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.list.error", null, locale));
        }

        return "views/province/province-list";
    }

    // ===========================================================
    // FORMULARIO NUEVA PROVINCIA
    // ===========================================================
    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {

        logger.info("Mostrando formulario para nueva provincia.");

        try {
            List<RegionDTO> listRegions =
                    RegionMapper.toDTOList(regionDAO.listAllRegions());

            // ⭐ CORRECCIÓN CLAVE 1: Usar ProvinceUpdateDTO, que sí tiene el campo 'id' (null en este caso)
            model.addAttribute("province", new ProvinceUpdateDTO());
            model.addAttribute("listRegions", listRegions);

        } catch (Exception e) {
            logger.error("Error al cargar regiones para formulario: {}", e.getMessage());
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.edit.error", null, locale));
        }

        return "views/province/province-form";
    }

    // ===========================================================
    // INSERTAR
    // ===========================================================
    @PostMapping("/insert")
    public String insertProvince(
            // ⭐ CORRECCIÓN CLAVE 2: Usar ProvinceUpdateDTO para el formulario de inserción
            @Valid @ModelAttribute("province") ProvinceUpdateDTO provinceDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model, Locale locale) {

        logger.info("Insertando provincia con código {}", provinceDTO.getCode());

        try {
            if (result.hasErrors()) {
                model.addAttribute("listRegions",
                        RegionMapper.toDTOList(regionDAO.listAllRegions()));
                return "views/province/province-form";
            }

            if (provinceDAO.existsProvinceByCode(provinceDTO.getCode())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.province-controller.insert.codeExist", null, locale));
                return "redirect:/provinces/new";
            }

            // 1. Obtener la entidad Region usando el ID del DTO
            Region regionEntity = regionDAO.getRegionById(provinceDTO.getRegionId());
            if (regionEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: La Región seleccionada no existe.");
                return "redirect:/provinces/new";
            }

            // 2. Llamar al Mapper con la entidad Region
            // NOTA: Usamos el método toEntity(ProvinceUpdateDTO, Region) ya que ahora el modelo es un UpdateDTO
            Province entity = ProvinceMapper.toEntity(provinceDTO, regionEntity);
            provinceDAO.insertProvince(entity);

        } catch (Exception e) {
            logger.error("Error al insertar provincia: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.insert.error", null, locale));
        }

        return "redirect:/provinces";
    }

    // ===========================================================
    // FORMULARIO EDITAR
    // ===========================================================
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model, Locale locale) {

        logger.info("Mostrando formulario de edición para provincia id={}", id);

        try {
            Province entity = provinceDAO.getProvinceById(id);

            if (entity == null) {
                model.addAttribute("errorMessage",
                        messageSource.getMessage("msg.province-controller.edit.notfound", null, locale));
                return "redirect:/provinces";
            }

            ProvinceUpdateDTO dto = ProvinceMapper.toUpdateDTO(entity);
            List<RegionDTO> listRegions =
                    RegionMapper.toDTOList(regionDAO.listAllRegions());

            model.addAttribute("province", dto);
            model.addAttribute("listRegions", listRegions);

        } catch (Exception e) {
            logger.error("Error al obtener provincia: {}", e.getMessage());
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.edit.error", null, locale));
        }

        return "views/province/province-form";
    }

    // ===========================================================
    // UPDATE
    // ===========================================================
    @PostMapping("/update")
    public String updateProvince(@Valid @ModelAttribute("province") ProvinceUpdateDTO provinceDTO,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model, Locale locale) {

        logger.info("Actualizando provincia id={}", provinceDTO.getId());

        try {
            if (result.hasErrors()) {
                model.addAttribute("listRegions",
                        RegionMapper.toDTOList(regionDAO.listAllRegions()));
                return "views/province/province-form";
            }

            if (provinceDAO.existsProvinceByCodeAndNotId(provinceDTO.getCode(), provinceDTO.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.province-controller.update.codeExist", null, locale));
                return "redirect:/provinces/edit?id=" + provinceDTO.getId();
            }

            // 1. Obtener la entidad Region usando el ID del DTO
            Region regionEntity = regionDAO.getRegionById(provinceDTO.getRegionId());
            if (regionEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: La Región seleccionada para la actualización no existe.");
                return "redirect:/provinces/edit?id=" + provinceDTO.getId();
            }

            // 2. Llamar al Mapper con la entidad Region
            Province entity = ProvinceMapper.toEntity(provinceDTO, regionEntity);
            provinceDAO.updateProvince(entity);

        } catch (Exception e) {
            logger.error("Error al actualizar provincia: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.update.error", null, locale));
        }

        return "redirect:/provinces";
    }

    // ===========================================================
    // DELETE
    // ===========================================================
    @PostMapping("/delete")
    public String deleteProvince(@RequestParam("id") Long id,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {

        logger.info("Eliminando provincia id={}", id);

        try {
            provinceDAO.deleteProvince(id);
        } catch (Exception e) {
            logger.error("Error al eliminar provincia: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.delete.error", null, locale));
        }

        return "redirect:/provinces";
    }

    // ===========================================================
    // DETALLE
    // ===========================================================
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model, RedirectAttributes redirectAttributes,
                             Locale locale) {

        logger.info("Mostrando detalle provincia id={}", id);

        try {
            Province entity = provinceDAO.getProvinceById(id);

            if (entity == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.province-controller.detail.notFound", null, locale));
                return "redirect:/provinces";
            }

            ProvinceDetailDTO dto = ProvinceMapper.toDetailDTO(entity);
            model.addAttribute("province", dto);

        } catch (Exception e) {
            logger.error("Error al cargar detalle provincia: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.province-controller.detail.error", null, locale));
            return "redirect:/provinces";
        }

        return "views/province/province-detail";
    }
}