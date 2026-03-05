package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<User, Long> {

    // Buscar por email
    boolean existsByEmail(String email);

    // Para update: existe otro usuario con este email
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Busca un usuario por email y carga también sus roles en la misma consulta.
     *
     * <p>Se usa {@link EntityGraph} para asegurar que la relación {@code roles}
     * esté inicializada cuando Spring Security construya las authorities.</p>
     *
     * @param email email del usuario (username real del sistema)
     * @return Optional con el usuario y sus roles; Optional.empty() si no existe
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

}