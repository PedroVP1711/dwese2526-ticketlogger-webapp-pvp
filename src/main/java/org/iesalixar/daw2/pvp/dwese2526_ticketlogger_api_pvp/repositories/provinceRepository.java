package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.repositories;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface provinceRepository extends JpaRepository<Province, Long> {

    /**
     * Comprueba si existe una provincia con el código indicado.
     */
    boolean existsByCode(String code);

    /**
     * Comprueba si existe una provincia con el código indicado excluyendo un id.
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * Busca por id (ya lo hereda, pero lo dejamos explícito).
     */
    @Override
    Optional<Province> findById(Long id);
}