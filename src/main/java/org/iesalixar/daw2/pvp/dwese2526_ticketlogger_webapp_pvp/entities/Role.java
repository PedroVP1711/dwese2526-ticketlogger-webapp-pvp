package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;  // Asegúrate de importar esta anotación
import lombok.Data;

@Entity
@Data
public class Role {

    @Id  // Añadir esta anotación
    private Long id;

    private String name;

    public String getDisplayName() {
        return name;
    }
}
