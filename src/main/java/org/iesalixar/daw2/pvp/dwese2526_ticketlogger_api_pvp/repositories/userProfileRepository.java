package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DAO para la entidad {@link UserProfile}.
 *
 * Pensando para la funcionalidad de gestión de perfil de usuario ("Mi perfil")
 * donde normalmente se trabaja con un único perfil asociado a un usuario
 */

public interface userProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}