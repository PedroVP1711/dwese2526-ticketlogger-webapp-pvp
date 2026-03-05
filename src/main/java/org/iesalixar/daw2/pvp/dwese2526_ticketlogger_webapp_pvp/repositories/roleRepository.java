package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface roleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param name el nombre del rol a buscar.
     * @return un Optional que contiene el rol si se encuentra, o vacío si no existe.
     */
    Optional<Role> findByName(String name);
    // Buscar roles por un conjunto de IDs
    List<Role> findAllByIdIn(Set<Long> ids);

    // (Opcional) ver todos los roles, aunque findAll() ya existe en JpaRepository
    default List<Role> listAllRoles() {
        return findAll();
    }
}