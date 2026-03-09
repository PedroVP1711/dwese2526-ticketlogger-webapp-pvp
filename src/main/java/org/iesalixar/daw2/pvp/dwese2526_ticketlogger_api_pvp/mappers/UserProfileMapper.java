package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.mappers;
import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfileDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.UserProfile;

/**
 * Mapper utilitario entre la entidad {@Link UserProfile} y su DTO de formulario
 * {@Link UserProfileFormDTO}
 *
 *
 * Está pensando para la funcionalidad "Mi perfil", donde el mismo formulario
 * se usa tanto para crear el perfil (si no existe) como para editarlo.
 *
 * Implementación simple sin frameworks de mapeo.
 */

public class UserProfileMapper {

    /**
     * Convierte una combinación de {@Link User} + {@Link UserProfile} en un
     * {@link UserProfileDTO}.
     *
     * Si el perfil e null, se devuelve un DTO con datos básicos del User
     * (id, email) y el resto de campos vacíos, útil paara mostrar el formulario
     * de creación.
     *
     * @param user  Usuario autenticado (obligatorio).
     * @param profile Perfil del usuario (puede ser null).
     * @return DTO para el formulario de perfil.
     */
    public static UserProfileDTO toFormDto(User user, UserProfile profile) {
        if (user == null)  {
            return null;
        }

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());

        if (profile !=null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setProfileImage(profile.getProfileImage());
            dto.setBio(profile.getBio());
            dto.setLocale(profile.getLocale());
        }
        return dto;
    }

    /**
     * Crea una nueva entidad {@link org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.UserProfile} a partir de un
     * {@link UserProfileDTO} y un {@link User}.
     *
     * Pensado para el caso en el que el perfil aún no existe en base de datos.
     * El id se toma del User asociado mediante la anotación @MapsId
     *
     * @param dto DTO del formulario.
     * @param user Entidad User asociada (obligatoria).
     * @return Nueva cantidad UserProfile sin persistir.
     */
    public static UserProfile toNewEntity(UserProfileDTO dto, User user) {
        UserProfile profile = null;
        if (user == null || profile == null) {
            // crear DTO vacío

            return null;
        }
        profile = new UserProfile();
        profile.setUser(user);
        profile.setUser(user);
        profile.setId(user.getId());

        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setProfileImage(dto.getProfileImage());
        profile.setBio(dto.getBio());
        profile.setLocale(dto.getLocale());

        return profile;
    }

    /**
     * Copia los campos editables de {@link UserProfileDTO} sobre una
     * entidad {@link UserProfile} existente.
     * <p>
     * Recomendado para el caso de edición, manteniendo el estado de persistencia
     * y las propiedades gestionadas por la BD (created_at, updated_at, etc.).
     *
     * @param dto     DTO con los datos del formulario.
     * @param profile Entidad UserProfile existente (ya cargada de DB).
     */
    public static void copuToExistingEntity(UserProfileDTO dto, UserProfile profile) {

    }

    public static void copyToExistingEntity(@Valid UserProfileDTO profileDto, UserProfile profile) {
    }
}