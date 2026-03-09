package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.controllers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfileDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfilePatchDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services.userProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@Validated
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private userProfileService userProfileService;

    // --------------------------
    // GET perfil actual
    // --------------------------
    @GetMapping
    public ResponseEntity<UserProfileDTO> getMyProfile(Principal principal) {
        String email = principal.getName();
        logger.info("API getMyProfile para {}", email);

        UserProfileDTO dto = userProfileService.getFormByEmail(email);
        return ResponseEntity.ok(dto);
    }

    // --------------------------
    // PATCH perfil actual (multipart/form-data)
    // --------------------------
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDTO> patchMyProfile(
            @ModelAttribute UserProfilePatchDTO patchDto,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            Principal principal
    ) {
        String email = principal.getName();
        logger.info("API patchMyProfile para {}", email);

        try {
            userProfileService.updateProfile(email, patchDto, profileImageFile);
            UserProfileDTO updated = userProfileService.getFormByEmail(email);
            return ResponseEntity.ok(updated);

        } catch (ResourceNotFoundException ex) {
            logger.error("Usuario no encontrado para email={}", email);
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            logger.error("Error al actualizar perfil para email={}: {}", email, ex.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}