package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SCocheCreateDTO {
    private Integer numeroCoches;
    private Float kilometrajeTotal;
    private String marca;
    private String tipoGasolina;
}
