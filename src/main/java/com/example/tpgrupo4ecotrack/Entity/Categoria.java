package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false)
    private String nombreCategoria;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaCoche> coches;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaAlimento> alimentos;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaRopa> ropas;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaElectrodomestico> electrodomesticos;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaServicioVivienda> servicios;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<SubCategoriaAutobus> autobuses;
}
