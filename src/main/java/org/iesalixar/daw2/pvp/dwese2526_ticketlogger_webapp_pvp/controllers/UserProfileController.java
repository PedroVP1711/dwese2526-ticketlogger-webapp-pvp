package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserProfileMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.userProfileRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.userRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.services.FileStorageServices;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.services.userProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private userRepository userDAO;

    @Autowired
    private userProfileRepository userProfileDAO;

    @Autowired
    private FileStorageServices fileStorageServices;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private userProfileService userProfileService;
    // --------------------------
    // Mostrar formulario
    // --------------------------
    @GetMapping("/edit")
    public String showProfileForm(Model model, Locale locale, Principal principal) {
        String email = principal.getName();
        logger.info("Mostrando formulario de perfil para {}", email);

        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isEmpty()) {
            String errorMessage = messageSource.getMessage(
                    "msg.user-controller.edit.notfound", null, locale
            );
            model.addAttribute("errorMessage", errorMessage);
            return "views/user-profile/user-profile-form";
        }

        User user = userOpt.get();
        Optional<UserProfile> profileOpt = userProfileDAO.findByUserId(user.getId());

        UserProfileFormDTO formDto = UserProfileMapper.toFormDto(user, profileOpt.orElse(null));
        model.addAttribute("userProfileForm", formDto);

        return "views/user-profile/user-profile-form";
    }


    // --------------------------
    // Actualizar perfil
    // --------------------------
    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("userProfileForm") UserProfileFormDTO profileDto,
            BindingResult result,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            RedirectAttributes redirectAttributes,
            Locale locale,
            Principal principal) {

        String email = principal.getName();
        logger.info("Actualizando perfil para email={}", email);

        // 1) Si hay errores de validación, volver al formulario
        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario para email={}", email);
            return "views/user-profile/user-profile-form";
        }

        try {

            // 2) Delegar lógica al service (NO usar profileDto.getUserId())
            userProfileService.updateProfile(email, profileDto, profileImageFile);

            // 3) Mensaje de éxito
            String successMessage = messageSource.getMessage(
                    "msg.userProfile.success",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (ResourceNotFoundException ex) {

            logger.error("Usuario no encontrado para email={}", email);

            String errorMessage = messageSource.getMessage(
                    "msg.user-controller.edit.notfound",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/profile/edit";
        }

        return "redirect:/profile/edit";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        return "views/user-profile/change-password-form";
    }

}