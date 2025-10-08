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

    private String nombreItem;
    private String categoria;
    private Float cantidad;
    private Float emisionesKgCO2;

    @ManyToOne
    @JoinColumn(name = "resultado_id")
    private Resultado resultado;


    public void setAlimento(SubCategoriaAlimento a) {
    }
    public void setRopa(SubCategoriaRopa r) {
    }
    public void setCoche(SubCategoriaCoche c) {
    }
    public void setAutobus(SubCategoriaAutobus b) {
    }
    public void setElectrodomestico(SubCategoriaElectrodomestico e) {
    }

    public void setServicio(SubCategoriaServicioVivienda s) {
    }
}