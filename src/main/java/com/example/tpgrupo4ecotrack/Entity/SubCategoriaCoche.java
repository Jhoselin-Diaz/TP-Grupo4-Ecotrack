package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "subcategoria_coche")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaCoche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoche;

    private Integer numeroCoches;
    private Float kilometrajeTotal;
    private String marca;
    private String tipoGasolina;
    private Float emisionesKgCO2_C;
    private Boolean enviadoResultadoC;
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
