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

    public List<ResultadoDetalleDTO> obtenerDetalles() {
        log.info("Obteniendo lista de detalles de resultados");
        List<ResultadoDetalle> lista = resultadoDetalleRepository.findAll();
        List<ResultadoDetalleDTO> dtoList = new ArrayList<>();
        for (ResultadoDetalle d : lista) {
            ResultadoDetalleDTO dto = new ResultadoDetalleDTO();
            dto.setIdDetalle(d.getIdDetalle());
            dto.setEmisionesKgCO2((double) d.getEmisionesKgCO2());
            if (d.getResultado() != null) dto.setResultadoId(d.getResultado().getIdResultado());
            if (d.getServicio() != null) dto.setServicioId(d.getServicio().getIdServicios());
            if (d.getCoche() != null) dto.setCocheId(d.getCoche().getIdCoche());
            if (d.getAutobus() != null) dto.setAutobusId(d.getAutobus().getIdAutobus());
            if (d.getAlimento() != null) dto.setAlimentoId(d.getAlimento().getIdAlimento());
            if (d.getElectrodomestico() != null) dto.setElectrodomesticoId(d.getElectrodomestico().getIdElectrodomestico());
            if (d.getRopa() != null) dto.setRopaId(d.getRopa().getIdRopa());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public ResultadoDetalle insertar(ResultadoDetalleDTO dto) {
        log.info("Insertando detalle de resultado");
        ResultadoDetalle d = new ResultadoDetalle();
        d.setEmisionesKgCO2(dto.getEmisionesKgCO2().floatValue());

        if (dto.getResultadoId() != null) {
            Resultado r = new Resultado();
            r.setIdResultado(dto.getResultadoId());
            d.setResultado(r);
        }
        if (dto.getServicioId() != null) {
            SubCategoriaServicioVivienda s = new SubCategoriaServicioVivienda();
            s.setIdServicios(dto.getServicioId());
            d.setServicio(s);
        }
        if (dto.getCocheId() != null) {
            SubCategoriaCoche c = new SubCategoriaCoche();
            c.setIdCoche(dto.getCocheId());
            d.setCoche(c);
        }
        if (dto.getAutobusId() != null) {
            SubCategoriaAutobus a = new SubCategoriaAutobus();
            a.setIdAutobus(dto.getAutobusId());
            d.setAutobus(a);
        }
        if (dto.getAlimentoId() != null) {
            SubCategoriaAlimento al = new SubCategoriaAlimento();
            al.setIdAlimento(dto.getAlimentoId());
            d.setAlimento(al);
        }
        if (dto.getElectrodomesticoId() != null) {
            SubCategoriaElectrodomestico e = new SubCategoriaElectrodomestico();
            e.setIdElectrodomestico(dto.getElectrodomesticoId());
            d.setElectrodomestico(e);
        }
        if (dto.getRopaId() != null) {
            SubCategoriaRopa r = new SubCategoriaRopa();
            r.setIdRopa(dto.getRopaId());
            d.setRopa(r);
        }

        return resultadoDetalleRepository.save(d);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando detalle con ID: {}", id);
        resultadoDetalleRepository.deleteById(id);
        return "Registro eliminado";
    }
}
