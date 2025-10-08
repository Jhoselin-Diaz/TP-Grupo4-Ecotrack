package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDetalleDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.ResultadoDetalleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResultadoDetalleService {

    private final ResultadoDetalleRepository resultadoDetalleRepository;

    public ResultadoDetalleService(ResultadoDetalleRepository resultadoDetalleRepository) {

        this.resultadoDetalleRepository = resultadoDetalleRepository;
    }

    public <T> float crearDetallesPorCategoria(Resultado resultado, List<T> registros) {
        float totalEmisiones = 0f;

        for (T registro : registros) {
            ResultadoDetalle detalle = new ResultadoDetalle();
            detalle.setResultado(resultado);

            if (registro instanceof SubCategoriaAlimento a) {
                detalle.setEmisionesKgCO2(a.getEmisionesKgCO2_AL());
                detalle.setAlimento(a);
                totalEmisiones += a.getEmisionesKgCO2_AL();
            } else if (registro instanceof SubCategoriaRopa r) {
                detalle.setEmisionesKgCO2(r.getEmisionesKgCO2_R());
                detalle.setRopa(r);
                totalEmisiones += r.getEmisionesKgCO2_R();
            } else if (registro instanceof SubCategoriaCoche c) {
                detalle.setEmisionesKgCO2(c.getEmisionesKgCO2_C());
                detalle.setCoche(c);
                totalEmisiones += c.getEmisionesKgCO2_C();
            } else if (registro instanceof SubCategoriaAutobus b) {
                detalle.setEmisionesKgCO2(b.getEmisionesKgCO2_A());
                detalle.setAutobus(b);
                totalEmisiones += b.getEmisionesKgCO2_A();
            } else if (registro instanceof SubCategoriaElectrodomestico e) {
                detalle.setEmisionesKgCO2(e.getEmisionesKgCO2_E());
                detalle.setElectrodomestico(e);
                totalEmisiones += e.getEmisionesKgCO2_E();
            } else if (registro instanceof SubCategoriaServicioVivienda s) {
                detalle.setEmisionesKgCO2(s.getEmisionesKgCO2_S());
                detalle.setServicio(s);
                totalEmisiones += s.getEmisionesKgCO2_S();
            }

            resultadoDetalleRepository.save(detalle);
        }

        return totalEmisiones;
    }

    public String eliminar(Long id) {
        log.warn("Eliminando detalle con ID: {}", id);
        resultadoDetalleRepository.deleteById(id);
        return "Registro eliminado";
    }
}
