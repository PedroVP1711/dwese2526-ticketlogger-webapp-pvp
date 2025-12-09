package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = false
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Province> provinces = new ArrayList<>();

    // Constructor vacío manual eliminado, Lombok lo genera con @NoArgsConstructor
}
