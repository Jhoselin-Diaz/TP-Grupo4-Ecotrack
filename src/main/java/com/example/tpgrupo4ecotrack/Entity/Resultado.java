package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "resultados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    @ManyToOne
    @JoinColumn(name = "huella_id")
    private HuellaCarbono huella;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL)
    private List<ResultadoDetalle> detalles;

    @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL)
    private List<ResultadoEquivalencia> equivalencias;
}
