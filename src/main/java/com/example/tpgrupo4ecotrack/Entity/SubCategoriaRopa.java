package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "subcategoria_ropa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaRopa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRopa;

    private String tipoPrenda;
    private Float cantidadKg;
    private Float emisionesKgCO2_R;
    private Boolean enviadoResultadoR;
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
