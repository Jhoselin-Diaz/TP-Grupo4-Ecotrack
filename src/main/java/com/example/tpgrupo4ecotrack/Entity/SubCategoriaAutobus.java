package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "subcategoria_autobus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaAutobus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutobus;

    private Float autobusKm;
    private Float autocarKm;
    private Float trenNacionalKm;
    private Float tranviaKm;
    private Float metroKm;
    private Float taxiKm;
    private float emisionesKgCO2_A;
    private boolean enviadoResultadoA;
    private Date fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "factor_id")
    private FactorEmision factor;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}

