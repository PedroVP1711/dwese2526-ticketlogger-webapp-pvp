package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserProfileDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controlador para la funcionalidad de perfil de usuario ("Mi perfil").
 *
 * No es un CRUD clásico, sino un único formulario que permite crear o
 * actualizar el perfil del usuario.
 */

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserProfileDAO userProfileDAO;

    /**
     * Muestra el formulario de perfil para el usuario indicado.
     * Si el perfil no existe, se mostrarán los campos vacíos (alta).
     * Si existe, se precargarán los datos (edición).
     *
     * @param model Modelo para pasar datos a la vista.
     * @param locale Locale actual para mensajes traducidos.
     * @return Plantilla Thymeleaf del formulario del perfil.
     */
    @GetMapping("/edit")
    public String showProfileForm(Model model, Locale locale) {
        final String fixedEmail = "admin@app.local";
        logger.info("Mostrando formulario de perfil para el usuario fijo {}", fixedEmail);

        User user = userDAO.getUserByEmail(fixedEmail);
        if (user == null) {
            logger.warn("No se encontró el usuario con email {}" , fixedEmail);
            String erroMessage = messageSource.getMessage("msg.user-controller.edit.notfound", null, locale);
            model.addAttribute("errorMessage", erroMessage);
            return "views/user-profile/user-profile-form";
        }
        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());
        UserProfileFormDTO formDto = UserProfileMapper.toFormDto(user, profile);
        model.addAttribute("userProfileForm", formDto);

        return "views/user-profile/user-profile-form";
    }

    /**
     * Procesa el envío del formulario de perfil de usuario.
     * Si el perfil no existe, lo crea. Si existe, lo actualiza.
     *
     * @param profileDto              DTO con los datos del formulario.
     * @param result                  Resultado de la validación.
     * @param redirectAttributes      Atributos para mensajes flash.
     * @param locale                  Locale actual para mensajes traducidos
     * @return Redirección al propio formulario de perfil (para mostrar mensaje)
     */

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute("userProfileForm") UserProfileFormDTO profileDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {


        logger.info("Actualizando perfil para el usuario de perfil para iserID={}", profileDto.getUserId());

        if (result.hasErrors()) {
            logger.warn("Errores de validación para el usuario con ID {}", profileDto.getUserId());
            return "views/user-profile/user-profile-form";
        }
        try {
            Long userId = profileDto.getUserId();
            User user = userDAO.getUserById(userId);
            if(user ==null){
                logger.warn("No se encontró el usuario con ID {}",userId);
                String errorMessage = messageSource.getMessage(
                        "msg.user-controller.edit.notfound",
                        null,
                        locale
                );
                redirectAttributes.addFlashAttribute("errorMessage",errorMessage);
                return"redirect:/profile/edit";
            }

            UserProfile profile = userProfileDAO.getUserProfileByUserId(userId);
            boolean isNew = (profile == null);
            if(isNew) {
                profile =UserProfileMapper.toNewEntity(profileDto, user);
            } else{
                UserProfileMapper.copyToExistingEntity(profileDto, profile);
            }
            userProfileDAO.saveOrUpdateUserProfile(profile);

            String successMessage = messageSource.getMessage("msg.userProfile.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage",successMessage);
        } catch (Exception e) {
            logger.error("Error al actualizar el perfil del usuario on ID {}: {}",
                    profileDto.getUserId(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage(
                    "msg.userProfile.error",
                    null,
                    locale
            );
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/profile/edit";
    }


}