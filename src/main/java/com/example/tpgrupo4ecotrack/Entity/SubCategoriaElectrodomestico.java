package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "subcategoria_electrodomestico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaElectrodomestico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idElectrodomestico;

    private String tipoElectrodomestico;
    private Float consumoKWh;
    private Float emisionesKgCO2_E;
    private Boolean enviadoResultadoE;
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
