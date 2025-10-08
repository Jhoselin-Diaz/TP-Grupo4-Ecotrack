package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SServicioCreateDTO {
    private Float electricidadKWh;
    private Float gasNaturalM3;
    private Float carbonKl;
}