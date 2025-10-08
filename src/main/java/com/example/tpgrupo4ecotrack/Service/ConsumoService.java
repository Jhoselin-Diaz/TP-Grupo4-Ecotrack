package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.ConsumoDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConsumoService {
    private final SAlimentoRepository alimentoRepo;
    private final SRopaRepository ropaRepo;
    private final SElectrodomesticoRepository electroRepo;
    private final SCocheRepository cocheRepo;
    private final SAutobusRepository autobusRepo;
    private final SServicioViviendaRepository servicioRepo;


    public ConsumoService(SAlimentoRepository alimentoRepo,
                          SRopaRepository ropaRepo,
                          SElectrodomesticoRepository electroRepo,
                          SCocheRepository cocheRepo, SAutobusRepository autobusRepo,
                          SServicioViviendaRepository servicioRepo) {
        this.alimentoRepo = alimentoRepo;
        this.ropaRepo = ropaRepo;
        this.electroRepo = electroRepo;
        this.cocheRepo = cocheRepo;
        this.autobusRepo = autobusRepo;
        this.servicioRepo = servicioRepo;
    } // Helper: convierte Number (Double/Float/primitivos) a Float seguro


    private Float toFloat(Number value) {

        return value == null ? 0f : value.floatValue();
    }


    public List<ConsumoDTO> getConsumos(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<ConsumoDTO> resultado = new ArrayList<>();

        // -------- Alimentos --------
        List<SubCategoriaAlimento> alimentos = alimentoRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (alimentos != null && !alimentos.isEmpty()) {
            for (SubCategoriaAlimento a : alimentos) {
                resultado.add(new ConsumoDTO(
                        "Alimento",
                        a.getNombreAlimento(),
                        toFloat(a.getCantidadKg()),
                        toFloat(a.getEmisionesKgCO2_AL())
                ));
            }
        }

        // -------- Ropa --------
        List<SubCategoriaRopa> ropas = ropaRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (ropas != null && !ropas.isEmpty()) {
            for (SubCategoriaRopa r : ropas) {
                resultado.add(new ConsumoDTO(
                        "Ropa",
                        r.getTipoPrenda(),
                        toFloat(r.getCantidadKg()),
                        toFloat(r.getEmisionesKgCO2_R())
                ));
            }
        }

        // -------- Electrodomésticos --------
        List<SubCategoriaElectrodomestico> electros = electroRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (electros != null && !electros.isEmpty()) {
            for (SubCategoriaElectrodomestico e : electros) {
                resultado.add(new ConsumoDTO(
                        "Electrodomestico",
                        e.getTipoElectrodomestico(),
                        toFloat(e.getConsumoKWh()),
                        toFloat(e.getEmisionesKgCO2_E())
                ));
            }
        }

        // -------- Coches --------
        List<SubCategoriaCoche> coches = cocheRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (coches != null && !coches.isEmpty()) {
            for (SubCategoriaCoche c : coches) {
                resultado.add(new ConsumoDTO(
                        "Coche",
                        c.getMarca() != null ? c.getMarca() : "Coche",
                        toFloat(c.getKilometrajeTotal()),
                        toFloat(c.getEmisionesKgCO2_C())
                ));
            }
        }

        // -------- Autobuses / Transporte público (varios campos de km) --------
        List<SubCategoriaAutobus> autobuses = autobusRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (autobuses != null && !autobuses.isEmpty()) {
            for (SubCategoriaAutobus b : autobuses) {
                float autobusKm = b.getAutobusKm() == null ? 0f : b.getAutobusKm();
                float autocarKm = b.getAutocarKm() == null ? 0f : b.getAutocarKm();
                float trenKm = b.getTrenNacionalKm() == null ? 0f : b.getTrenNacionalKm();
                float tranviaKm = b.getTranviaKm() == null ? 0f : b.getTranviaKm();
                float metroKm = b.getMetroKm() == null ? 0f : b.getMetroKm();
                float taxiKm = b.getTaxiKm() == null ? 0f : b.getTaxiKm();

                float totalKm = autobusKm + autocarKm + trenKm + tranviaKm + metroKm + taxiKm;
                float totalEmisiones = toFloat(b.getEmisionesKgCO2_A());

                if (autobusKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (autobusKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Autobus", autobusKm, emis));
                }
                if (autocarKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (autocarKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Autocar", autocarKm, emis));
                }
                if (trenKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (trenKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Tren Nacional", trenKm, emis));
                }
                if (tranviaKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (tranviaKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Tranvía", tranviaKm, emis));
                }
                if (metroKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (metroKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Metro", metroKm, emis));
                }
                if (taxiKm > 0f) {
                    float emis = totalKm > 0f ? totalEmisiones * (taxiKm / totalKm) : 0f;
                    resultado.add(new ConsumoDTO("Autobus", "Taxi", taxiKm, emis));
                }
            }
        }

        // -------- Servicios de Vivienda (varios campos) --------
        List<SubCategoriaServicioVivienda> servicios = servicioRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (servicios != null && !servicios.isEmpty()) {
            for (SubCategoriaServicioVivienda s : servicios) {
                float electr = s.getElectricidadKWh() == null ? 0f : s.getElectricidadKWh();
                float gas = s.getGasNaturalM3() == null ? 0f : s.getGasNaturalM3();
                float carbon = s.getCarbonKl() == null ? 0f : s.getCarbonKl();
                float total = electr + gas + carbon;
                float totalEm = toFloat(s.getEmisionesKgCO2_S());

                if (electr > 0f) {
                    float emis = total > 0f ? totalEm * (electr / total) : 0f;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Electricidad", electr, emis));
                }
                if (gas > 0f) {
                    float emis = total > 0f ? totalEm * (gas / total) : 0f;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Gas Natural", gas, emis));
                }
                if (carbon > 0f) {
                    float emis = total > 0f ? totalEm * (carbon / total) : 0f;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Carbón", carbon, emis));
                }
            }
        }

        return resultado;
    }



}


