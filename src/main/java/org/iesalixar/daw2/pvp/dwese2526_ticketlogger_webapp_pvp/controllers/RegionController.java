package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.RegionDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
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

    @GetMapping("")
    public String listRegions(Model model) {
        List<Region> listRegions = regionDAO.listAllRegions();
        model.addAttribute("listRegions", listRegions);
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
    public String deleteRegion(@RequestParam("id") Long id) {
        regionDAO.deleteRegion(id);
        return "redirect:/regions";
    }
}
