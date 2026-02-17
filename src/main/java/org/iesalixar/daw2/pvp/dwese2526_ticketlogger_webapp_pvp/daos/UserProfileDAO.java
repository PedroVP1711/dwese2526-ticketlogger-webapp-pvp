package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

/**
 * DAO para la entidad {@link UserProfile}.
 *
 * Pensando para la funcionalidad de gestión de perfil de usuario ("Mi perfil")
 * donde normalmente se trabaja con un único perfil asociado a un usuario
 */

public interface UserProfileDAO {

    UserProfile getUserProfileByUserId(Long userId);
    void saveOrUpdateUserProfile(UserProfile userProfile);
    boolean existsUserProfileByUserId(Long userId);
}