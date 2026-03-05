package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.provinceRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.regionRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.*;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Province;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Region;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.ProvinceMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private provinceRepository provinceDAO;

    @Autowired
    private regionRepository regionDAO;

    // ===========================================================
    // LISTADO
    // ===========================================================
    @GetMapping
    public String listProvinces(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        logger.info("Listando provincias page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try {
            Page<ProvinceDTO> pageProvinces =
                    provinceDAO.findAll(pageable).map(ProvinceMapper::toDTO);

            model.addAttribute("page", pageProvinces);

            String sortParam = "name,asc";
            if (pageProvinces.getSort().isSorted()) {
                Sort.Order order = pageProvinces.getSort().iterator().next();
                sortParam = order.getProperty() + "," +
                        order.getDirection().name().toLowerCase();
            }

            model.addAttribute("sortParam", sortParam);

        } catch (Exception e) {
            logger.error("Error al listar provincias: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error al listar las provincias.");
        }

        return "views/province/province-list";
    }

    // ===========================================================
    // FORMULARIO NUEVA PROVINCIA
    // ===========================================================
    @GetMapping("/new")
    public String showNewForm(Model model) {

        model.addAttribute("province", new ProvinceCreateDTO());
        model.addAttribute("listRegions",
                regionDAO.findAll().stream()
                        .map(RegionMapper::toDTO)
                        .toList());

        return "views/province/province-form";
    }

    // ===========================================================
    // INSERTAR
    // ===========================================================
    @PostMapping("/insert")
    public String insertProvince(
            @Valid @ModelAttribute("province") ProvinceCreateDTO provinceDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale,
            Model model) {

        logger.info("Insertando provincia con código {}", provinceDTO.getCode());

        try {

            if (result.hasErrors()) {
                model.addAttribute("listRegions",
                        regionDAO.findAll().stream()
                                .map(RegionMapper::toDTO)
                                .toList());
                return "views/province/province-form";
            }

            if (provinceDAO.existsByCode(provinceDTO.getCode())) {
                String errorMessage = messageSource.getMessage(
                        "msg.province-controller.insert.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/provinces/new";
            }

            Optional<Region> regionOpt = regionDAO.findById(provinceDTO.getRegionId());
            if (regionOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "La región seleccionada no existe.");
                return "redirect:/provinces/new";
            }

            Province entity = ProvinceMapper.toEntity(provinceDTO, regionOpt.get());
            provinceDAO.save(entity);

        } catch (Exception e) {
            logger.error("Error al insertar provincia: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.insert.error", null, locale));
        }

        return "redirect:/provinces";
    }

    // ===========================================================
    // FORMULARIO EDITAR
    // ===========================================================
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        Optional<Province> provinceOpt = provinceDAO.findById(id);

        if (provinceOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.edit.notfound", null, locale));
            return "redirect:/provinces";
        }

        ProvinceUpdateDTO dto =
                ProvinceMapper.toUpdateDTO(provinceOpt.get());

        model.addAttribute("province", dto);
        model.addAttribute("listRegions",
                regionDAO.findAll().stream()
                        .map(RegionMapper::toDTO)
                        .toList());

        return "views/province/province-form";
    }

    // ===========================================================
    // UPDATE
    // ===========================================================
    @PostMapping("/update")
    public String updateProvince(
            @Valid @ModelAttribute("province") ProvinceUpdateDTO provinceDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Locale locale,
            Model model) {

        try {

            if (result.hasErrors()) {
                model.addAttribute("listRegions",
                        regionDAO.findAll().stream()
                                .map(RegionMapper::toDTO)
                                .toList());
                return "views/province/province-form";
            }

            if (provinceDAO.existsByCodeAndIdNot(
                    provinceDTO.getCode(), provinceDTO.getId())) {

                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage(
                                "msg.province-controller.update.codeExist",
                                null, locale));

                return "redirect:/provinces/edit?id=" + provinceDTO.getId();
            }

            Optional<Province> provinceOpt =
                    provinceDAO.findById(provinceDTO.getId());

            if (provinceOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage(
                                "msg.province-controller.detail.notFound",
                                null, locale));
                return "redirect:/provinces";
            }

            Optional<Region> regionOpt =
                    regionDAO.findById(provinceDTO.getRegionId());

            if (regionOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "La región seleccionada no existe.");
                return "redirect:/provinces/edit?id=" + provinceDTO.getId();
            }

            Province entity = provinceOpt.get();
            ProvinceMapper.copyToExistingEntity(
                    provinceDTO, entity, regionOpt.get());

            provinceDAO.save(entity);

        } catch (Exception e) {
            logger.error("Error al actualizar provincia: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.update.error",
                            null, locale));
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

        try {
            if (!provinceDAO.existsById(id)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage(
                                "msg.province-controller.detail.notFound",
                                null, locale));
                return "redirect:/provinces";
            }

            provinceDAO.deleteById(id);

        } catch (Exception e) {
            logger.error("Error al eliminar provincia: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage(
                            "msg.province-controller.delete.error",
                            null, locale));
        }

        return "redirect:/provinces";
    }

    // ===========================================================
    // DETALLE
    // ===========================================================
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {

        logger.info("Mostrando detalle de la provincia con ID {}", id);

        try {

            Optional<Province> provinceOpt = provinceDAO.findById(id);

            if (provinceOpt.isEmpty()) {
                String msg = messageSource.getMessage(
                        "msg.province-controller.detail.notFound",
                        null,
                        locale
                );
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/provinces";
            }

            Province province = provinceOpt.get();

            ProvinceDetailDTO dto = ProvinceMapper.toDetailDTO(province);
            model.addAttribute("province", dto);

            return "views/province/province-detail";

        } catch (Exception e) {
            logger.error("Error al obtener el detalle de la provincia {}: {}", id, e.getMessage(), e);

            String msg = messageSource.getMessage(
                    "msg.province-controller.detail.error",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces";
        }
    }
}