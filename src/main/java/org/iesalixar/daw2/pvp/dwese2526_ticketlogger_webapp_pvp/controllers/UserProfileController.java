package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserProfileDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserProfileMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.services.FileStorageService;
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

import java.util.Locale;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserProfileDAO userProfileDAO; // DAO de perfil inyectado

    @Autowired(required = false)
    private FileStorageService fileStorageService;


    @GetMapping("/edit")
    public String showProfileForm(Model model, Locale locale) {
        final String fixedEmail = "admin@app.local";

        User user = userDAO.getUserByEmail(fixedEmail);

        if (user == null) {
            logger.warn("No se encontró el usuario con email {}", fixedEmail);
            String errorMessage = messageSource.getMessage("msg.user-controller.edit.notfound", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            return "views/user-profile/user-profile-form";
        }

        UserProfile profile = user.getUserProfile();

        // Lógica para crear el perfil si no existe (importante para el modelo 1:1)
        if (profile == null) {
            profile = new UserProfile(user);
            user.setUserProfile(profile);
            userProfileDAO.save(profile);
        }

        // CORRECCIÓN 1: Se usa el método toFormDto(UserProfile)
        UserProfileFormDTO formDto = UserProfileMapper.toFormDto(profile);

        if (!model.containsAttribute("userProfileForm")) {
            model.addAttribute("userProfileForm", formDto);
        }

        return "views/user-profile/user-profile-form";
    }

    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("userProfileForm") UserProfileFormDTO profileDto,
            BindingResult result,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (result.hasErrors()) {
            logger.warn("Errores de validación en el formulario de perfil para userId={}", profileDto.getUserId());
            return "views/user-profile/user-profile-form";
        }

        try {
            Long userId = profileDto.getUserId();
            User user = userDAO.findById(userId);

            if (user == null) { /* ... manejo de error ... */ }

            UserProfile profile = user.getUserProfile();
            if (profile == null) { /* ... manejo de error (aunque la lógica de GET ya lo crea) ... */ }

            // CORRECCIÓN 2: oldImagePath se obtiene del UserProfile
            String oldImagePath = profile.getProfileImagePath();

            if (fileStorageService != null && profileImageFile != null && !profileImageFile.isEmpty()) {

                // ... (código de validación de archivo) ...

                String newImageWebPath = fileStorageService.saveFile(profileImageFile);

                if (newImageWebPath == null) {
                    // ... manejo de error al guardar
                    return "redirect:/profile/edit";
                }

                profileDto.setProfileImagePath(newImageWebPath);

                if (oldImagePath != null && !oldImagePath.isBlank() && !oldImagePath.equals(newImageWebPath)) {
                    fileStorageService.deleteFile(oldImagePath);
                }
            } else if (profileImageFile != null && profileImageFile.isEmpty()) {
                profileDto.setProfileImagePath(oldImagePath);
            }

            // CORRECCIÓN 3: Mapear los datos del DTO al objeto UserProfile existente
            UserProfileMapper.copyToExistingEntity(profileDto, profile);

            userProfileDAO.update(profile); // Se actualiza el perfil en BBDD

            // Nota: Si se permite editar el email/username, se debería actualizar también el objeto User
            // user.setEmail(profileDto.getEmail());
            // userDAO.updateUser(user);

            String successMessage = messageSource.getMessage("msg.userProfile.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

            return "redirect:/profile/edit";
        } catch (Exception e) {
            logger.error("Error al actualizar el perfil del usuario con ID: {}", profileDto.getUserId(), e);

            String errorMessage = messageSource.getMessage("msg.userProfile.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

            return "redirect:/profile/edit";
        }
    }
}