package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoDTO {
    
    private String categoria;
    private String nombre;
    private float cantidad;
    private float emisiones;

    public ConsumoDTO(String categoria, String nombre, Float cantidad, Float emisiones) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.emisiones = emisiones;
    }

}
