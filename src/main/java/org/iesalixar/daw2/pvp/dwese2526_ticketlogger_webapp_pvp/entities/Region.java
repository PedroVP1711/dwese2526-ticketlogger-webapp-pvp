package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * La clase {@code Region} representa una entidad que modela una región dentro de la base de datos.
 * Contiene tres campos: {@code id}, {@code code} y {@code name}, donde:
 * - {@code id} es el identificador único (clave primaria),
 * - {@code code} es un código corto de la región,
 * - {@code name} es el nombre completo de la región.
 *
 * Las anotaciones de validación se utilizan para asegurar que los datos sean correctos
 * antes de ser guardados o actualizados en la base de datos.
 *
 * Los mensajes de error están internacionalizados mediante las claves definidas en
 * los archivos {@code messages_es.properties} y {@code messages_en.properties}.
 */
public class Region {

    /** Identificador único de la región. Autogenerado por la base de datos. */
    private Long id;

    /** Código corto de la región (por ejemplo, "01" para Andalucía). */
    @NotEmpty(message = "{msg.region.code.notEmpty}")
    @Size(max = 2, message = "{msg.region.code.size}")
    private String code;

    /** Nombre completo de la región (por ejemplo, "Andalucía" o "Cataluña"). */
    @NotEmpty(message = "{msg.region.name.notEmpty}")
    @Size(max = 100, message = "{msg.region.name.size}")
    private String name;

    // -------------------------------
    // Constructores
    // -------------------------------

    /** Constructor vacío (necesario para frameworks como Spring o JPA). */
    public Region() {
    }

    /** Constructor con todos los campos. */
    public Region(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    /** Constructor sin ID (para inserciones nuevas). */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // -------------------------------
    // Getters y Setters
    // -------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -------------------------------
    // Métodos utilitarios opcionales
    // -------------------------------

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
