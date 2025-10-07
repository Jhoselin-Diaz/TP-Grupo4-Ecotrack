package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "subcategoria_servicio_vivienda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaServicioVivienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicios;

    private Float electricidadKWh;
    private Float gasNaturalM3;
    private Float gasolinaL;
    private Float carbonKl;
    private Float glpKl;
    private Float propanoKl;
    private Float emisionesKgCO2_S;
    private Boolean enviadoResultadoS;
    private LocalDateTime fechaRegistro;

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
