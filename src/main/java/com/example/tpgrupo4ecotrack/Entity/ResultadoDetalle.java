package com.example.tpgrupo4ecotrack.Entity;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "resultado_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private float emisionesKgCO2;

    @ManyToOne
    @JoinColumn(name = "id_resultado")
    private Resultado resultado;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private SubCategoriaServicioVivienda servicio;

    @ManyToOne
    @JoinColumn(name = "id_coche")
    private SubCategoriaCoche coche;

    @ManyToOne
    @JoinColumn(name = "id_autobus")
    private SubCategoriaAutobus autobus;

    @ManyToOne
    @JoinColumn(name = "id_alimento")
    private SubCategoriaAlimento alimento;

    @ManyToOne
    @JoinColumn(name = "id_electrodomestico")
    private SubCategoriaElectrodomestico electrodomestico;

    @ManyToOne
    @JoinColumn(name = "id_ropa")
    private SubCategoriaRopa ropa;



}