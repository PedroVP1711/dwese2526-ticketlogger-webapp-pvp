package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfileDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfilePatchDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.exceptions.InvalidFileException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories.userProfileRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class userProfileServiceImpl implements userProfileService {

    private static final Logger logger = LoggerFactory.getLogger(userProfileServiceImpl.class);

    @Autowired
    private userRepository userRepository;

    @Autowired
    private userProfileRepository userProfileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public UserProfileDTO getFormByEmail(String email) {
        // aquí puedes devolver un DTO fake para arrancar la app
        return new UserProfileDTO();
    }

    /**
     * Aplica un PATCH al perfil del usuario autenticado:
     * solo actualiza los campos que vengan en el DTO (no-nulos) y, opcionalmente,
     * sustituye la imagen de perfil si se adjunta un fichero.
     *
     * @param email            email del usuario autenticado (Principal)
     * @param patchDto         datos parciales del perfil (campos nulos => no se modifican)
     * @param profileImageFile imagen opcional (si viene, se valida y se reemplaza)
     * @throws ResourceNotFoundException si no existe el usuario (por email)
     * @throws InvalidFileException     si la imagen no cumple validaciones o no se puede guardar
     */
    @Transactional
    public void updateProfile(String email, UserProfilePatchDTO patchDto, MultipartFile profileImageFile) {

        logger.info("Parchando perfil para email={}", email);

        // (1) Fuente de verdad: user por email (NO confiar en userId/email del cliente)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        Long userId = user.getId();

        // (2) Cargar perfil; si no existe, crearlo (PATCH upsert)
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return p;
                });

        // (3) Merge de campos: solo tocar si vienen (no-null)
        if (patchDto.getFirstName() != null) {
            profile.setFirstName(patchDto.getFirstName());
        }

        if (patchDto.getLastName() != null) {
            profile.setLastName(patchDto.getLastName());
        }

        if (patchDto.getPhoneNumber() != null) {
            profile.setPhoneNumber(patchDto.getPhoneNumber());
        }

        if (patchDto.getBio() != null) {
            profile.setBio(patchDto.getBio());
        }

        if (patchDto.getLocale() != null) {
            profile.setLocale(patchDto.getLocale());
        }

        // (4) Imagen: validar + guardar nueva + borrar anterior
        if (profileImageFile != null && !profileImageFile.isEmpty()) {

            // Validaciones semánticas
            validateProfileImage(profileImageFile);

            // Ruta anterior guardada en la entidad
            String oldImagePath = profile.getProfileImage();

            String newImageWebPath = fileStorageService.saveFile(profileImageFile);

            if (newImageWebPath == null || newImageWebPath.isBlank()) {
                throw new InvalidFileException(
                        "userProfile",
                        "profileImageFile",
                        profileImageFile.getOriginalFilename(),
                        "No se pudo guardar la imagen de perfil."
                );
            }

            profile.setProfileImage(newImageWebPath);

            // Borrar anterior si existía
            if (oldImagePath != null && !oldImagePath.isBlank() && !oldImagePath.equals(newImageWebPath)) {
                fileStorageService.deleteFile(oldImagePath);
            }
        }

        // (5) Persistir (save sirve tanto para nuevo como existente)
        userProfileRepository.save(profile);
    }
    private void validateProfileImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidFileException(
                    "userProfile",
                    "profileImageFile",
                    "null",
                    "La imagen está vacía."
            );
        }

        // Tamaño máximo 2MB
        long maxSize = 2 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new InvalidFileException(
                    "userProfile",
                    "profileImageFile",
                    file.getOriginalFilename(),
                    "La imagen supera el tamaño máximo permitido (2MB)."
            );
        }

        // Tipos permitidos
        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("image/jpg"))) {

            throw new InvalidFileException(
                    "userProfile",
                    "profileImageFile",
                    file.getOriginalFilename(),
                    "Tipo de archivo no permitido. Solo JPG o PNG."
            );
        }
    }
}