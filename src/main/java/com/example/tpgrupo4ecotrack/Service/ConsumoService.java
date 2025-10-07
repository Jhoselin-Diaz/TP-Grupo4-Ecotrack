package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.ConsumoDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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


    private Float toFloat(Number n) {
        return n == null ? 0f : n.floatValue();
    }


    public List<ConsumoDTO> getConsumos(Long usuarioId, Date fechaInicio, Date fechaFin) {
        List<ConsumoDTO> resultado = new ArrayList<>();


        // -------- Alimentos --------
        List<SubCategoriaAlimento> alimentos = alimentoRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (alimentos != null) { for (SubCategoriaAlimento a : alimentos) {
            resultado.add(new ConsumoDTO( "Alimento",
                    a.getNombreAlimento(),
                    toFloat(a.getCantidadKg()),
                    toFloat(a.getEmisionesKgCO2_AL()) )); } }

        // -------- Ropa --------
        List<SubCategoriaRopa> ropas = ropaRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (ropas != null) { for (SubCategoriaRopa r : ropas) {
            resultado.add(new ConsumoDTO( "Ropa",
                    r.getTipoPrenda(),
                    toFloat(r.getCantidadKg()),
                    toFloat(r.getEmisionesKgCO2_R()) )); } }

        // -------- Electrodomésticos --------
        List<SubCategoriaElectrodomestico> electros = electroRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (electros != null) { for (SubCategoriaElectrodomestico e : electros) {
            resultado.add(new ConsumoDTO( "Electrodomestico",
                    e.getTipoElectrodomestico(),
                    toFloat(e.getConsumoKWh()),
                    toFloat(e.getEmisionesKgCO2_E()) )); } }

        // -------- Coches --------
        List<SubCategoriaCoche> coches = cocheRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (coches != null) { for (SubCategoriaCoche c : coches) {
            resultado.add(new ConsumoDTO( "Coche",
                    c.getMarca() != null ? c.getMarca() : "Coche",
                    toFloat(c.getKilometrajeTotal()),
                    toFloat(c.getEmisionesKgCO2_C()) )); } }

        // -------- Autobuses / Transporte público (varios campos de km) --------
        List<SubCategoriaAutobus> autobuses = autobusRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (autobuses != null) {
            for (SubCategoriaAutobus b : autobuses) {
                double autobusKm = b.getAutobusKm() == null ? 0d : b.getAutobusKm();
                double autocarKm = b.getAutocarKm() == null ? 0d : b.getAutocarKm();
                double trenKm = b.getTrenNacionalKm() == null ? 0d : b.getTrenNacionalKm();
                double tranviaKm = b.getTranviaKm() == null ? 0d : b.getTranviaKm();
                double metroKm = b.getMetroKm() == null ? 0d : b.getMetroKm();
                double taxiKm = b.getTaxiKm() == null ? 0d : b.getTaxiKm();
                double totalKm = autobusKm + autocarKm + trenKm + tranviaKm + metroKm + taxiKm;
                double totalEmisiones = toFloat(b.getEmisionesKgCO2_A()); // Para cada tipo con km > 0, creamos una fila.

                if (autobusKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (autobusKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Autobus",
                            (float) autobusKm, (float) emis)); }

                if (autocarKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (autocarKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Autocar",
                            (float) autocarKm, (float) emis)); }

                if (trenKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (trenKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Tren Nacional",
                            (float) trenKm, (float) emis)); }

                if (tranviaKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (tranviaKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Tranvía",
                            (float) tranviaKm, (float) emis)); }

                if (metroKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (metroKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Metro",
                            (float) metroKm, (float) emis)); }

                if (taxiKm > 0) {
                    double emis = totalKm > 0 ? totalEmisiones * (taxiKm / totalKm) : 0;
                    resultado.add(new ConsumoDTO("Autobus", "Taxi",
                            (float) taxiKm, (float) emis)); } } }

        // -------- Servicios de Vivienda (varios campos) --------
        List<SubCategoriaServicioVivienda> servicios = servicioRepo.findByUsuarioAndFecha(usuarioId, fechaInicio, fechaFin);
        if (servicios != null) {
            for (SubCategoriaServicioVivienda s : servicios) {
                double electr = s.getElectricidadKWh() == null ? 0d : s.getElectricidadKWh();
                double gas = s.getGasNaturalM3() == null ? 0d : s.getGasNaturalM3();
                double gasolina = s.getGasolinaL() == null ? 0d : s.getGasolinaL();
                double carbon = s.getCarbonKl() == null ? 0d : s.getCarbonKl();
                double glp = s.getGlpKl() == null ? 0d : s.getGlpKl();
                double propano = s.getPropanoKl() == null ? 0d : s.getPropanoKl();
                double total = electr + gas + gasolina + carbon + glp + propano;
                double totalEm = toFloat(s.getEmisionesKgCO2_S());

                if (electr > 0) {
                    double emis = total > 0 ? totalEm * (electr / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Electricidad",
                            (float) electr, (float) emis));
                }

                if (gas > 0) {
                    double emis = total > 0 ? totalEm * (gas / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Gas Natural",
                            (float) gas, (float) emis));
                }

                if (gasolina > 0) {
                    double emis = total > 0 ? totalEm * (gasolina / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Gasolina",
                            (float) gasolina, (float) emis));
                }

                if (carbon > 0) {
                    double emis = total > 0 ? totalEm * (carbon / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Carbón",
                            (float) carbon, (float) emis));
                }

                if (glp > 0) {
                    double emis = total > 0 ? totalEm * (glp / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "GLP",
                            (float) glp, (float) emis));
                }

                if (propano > 0) {
                    double emis = total > 0 ? totalEm * (propano / total) : 0;
                    resultado.add(new ConsumoDTO("ServicioVivienda", "Propano",
                            (float) propano, (float) emis));
                }

            }
        }

        return resultado;

    }


}


