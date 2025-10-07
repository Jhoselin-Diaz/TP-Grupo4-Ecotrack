package com.example.tpgrupo4ecotrack.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SAutobusCreateDTO {
    private Float autobusKm;
    private Float autocarKm;
    private Float trenNacionalKm;
    private Float tranviaKm;
    private Float metroKm;
    private Float taxiKm;
}
