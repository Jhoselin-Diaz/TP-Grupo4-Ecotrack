package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "resultado_equivalencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoEquivalencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquivalencia;

    private String tipoEquivalencia;
    private float valorEquivalente;
    private String unidad;

    @ManyToOne
    @JoinColumn(name = "resultado_id")
    private Resultado resultado;
}
