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
    private Float cantidad;
    private Float emisiones;

}
