package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.RegionDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.RegionDTO; // Necesario para el listado
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.RegionDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.RegionMapper; // Necesario para el listado
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionDAO regionDAO;

    @Autowired
    private MessageSource messageSource;

    // ===========================================================
    // LISTADO (Paginación y Ordenación)
    // ===========================================================
    @GetMapping("")
    public String listRegions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model, Locale locale) {

        logger.info("Solicitando la lista de regiones... page={}, size={}, sortField={}, sortDir={}",
                page, size, sortField, sortDir);

        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        try {
            long totalElements = regionDAO.countRegions();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            if (totalPages > 0 && page >= totalPages) page = totalPages - 1;

            // 1. Llama al DAO con paginación y ordenación
            List<Region> entities = regionDAO.listRegionsPage(page, size, sortField, sortDir);

            // 2. Mapea a DTOs
            List<RegionDTO> dtos = RegionMapper.toDTOList(entities);

            // Atributos de la lista (usamos 'regions' y 'listRegions' por seguridad)
            model.addAttribute("regions", dtos);
            model.addAttribute("listRegions", dtos);

            // Atributos de paginación y ordenación
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);

            // Atributo para invertir la dirección de ordenación
            String reverseSortDir = sortDir.equalsIgnoreCase("asc") ? "desc" : "asc";
            model.addAttribute("reverseSortDir", reverseSortDir);

        } catch (Exception e) {
            logger.error("Error al listar las regiones: {}", e.getMessage(), e);
            model.addAttribute("errorMessage",
                    messageSource.getMessage("msg.region-controller.list.error", null, locale));
        }

        return "views/region/region-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("region", new Region());
        return "views/region/region-form";
    }

    @PostMapping("/save")
    public String insertRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nueva región con código {}", region.getCode());

        try {
            if (result.hasErrors()) {
                return "views/region/region-form";
            }

            if (regionDAO.existsRegionByCode(region.getCode())) {
                logger.warn("El código de la región {} ya existe.", region.getCode());
                String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/regions/new";
            }

            regionDAO.insertRegion(region);
            logger.info("Región {} insertada con éxito.", region.getCode());

        } catch (Exception e) {
            logger.error("Error al insertar la región {}: {}", region.getCode(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/regions";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        Region existingRegion = regionDAO.getRegionById(id);
        model.addAttribute("region", existingRegion);
        return "views/region/region-form";
    }

    @PostMapping("/update")
    public String updateRegion(
            @Valid @ModelAttribute("region") Region region,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Actualizando región con ID {}", region.getId());

        try {
            if (result.hasErrors()) {
                return "views/region/region-form";
            }

            if (regionDAO.existsRegionByCodeAndNotId(region.getCode(), region.getId())) {
                logger.warn("El código de la región {} ya existe para otra región.", region.getCode());
                String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/regions/edit?id=" + region.getId();
            }

            regionDAO.updateRegion(region);
            logger.info("Región con ID {} actualizada con éxito.", region.getId());

        } catch (Exception e) {
            logger.error("Error al actualizar la región con ID {}: {}", region.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/regions";
    }

    @GetMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            regionDAO.deleteRegion(id);
            logger.info("Región con ID {} eliminada con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la región con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.region-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/regions";
    }

    // ===========================================================
    // DETALLE
    // ===========================================================
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model, RedirectAttributes redirectAttributes,
                             Locale locale) {

        logger.info("Mostrando detalle región id={}", id);

        try {
            Region entity = regionDAO.getRegionById(id);

            if (entity == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.region-controller.detail.notFound", null, locale));
                return "redirect:/regions";
            }

            RegionDetailDTO dto = RegionMapper.toDetailDTO(entity);
            model.addAttribute("region", dto);

        } catch (Exception e) {
            logger.error("Error al cargar detalle región: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.region-controller.detail.error", null, locale));
            return "redirect:/regions";
        }

        return "views/region/region-detail";
    }

}