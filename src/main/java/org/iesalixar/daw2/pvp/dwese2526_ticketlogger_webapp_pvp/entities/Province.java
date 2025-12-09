package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * La clase {@code Province} representa una provincia dentro del sistema.
 *
 * Cada provincia tiene:
 * <ul>
 *   <li>{@code id}: identificador único (clave primaria, autogenerado en BD).</li>
 *   <li>{@code code}: código único de la provincia (ej. "SE", "MA", "GR").</li>
 *   <li>{@code name}: nombre de la provincia (ej. "Sevilla").</li>
 *   <li>{@code region}: objeto {@link Region} al que pertenece la provincia
 *       (equivalente a la FK {@code region_id} en la base de datos).</li>
 * </ul>
 *
 * Se sigue el mismo estilo que {@link Region}, usando Lombok y Bean Validation.
 * A nivel de BD seguirá existiendo la columna {@code region_id}, pero en la capa
 * de dominio trabajamos con el objeto {@code Region}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "provinces")
public class Province {

    // Identificador único de la provincia (AUTO_INCREMENT en la tabla `provinces`).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único de la provincia (VARCHAR(10) NOT NULL UNIQUE).
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    // Nombre de la provincia (VARCHAR(100) NOT NULL).
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Region asociada (puede venir instanciada pero con id null) por eso necesita una validación derivada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id",nullable = false)
    private Region region;



    /**
     * Constructor de conveniencia sin {@code id},
     * útil para altas donde el ID se autogenera en base de datos.
     *
     * @param code  código de la provincia.
     * @param name  nombre de la provincia.
     * @param region región asociada (debe tener al menos el id informado).
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}