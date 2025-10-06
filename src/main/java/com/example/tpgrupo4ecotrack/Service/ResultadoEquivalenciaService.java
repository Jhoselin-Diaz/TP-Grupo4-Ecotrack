package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ResultadoEquivalenciaDTO;
import com.example.tpgrupo4ecotrack.Entity.Resultado;
import com.example.tpgrupo4ecotrack.Entity.ResultadoEquivalencia;
import com.example.tpgrupo4ecotrack.Repository.ResultadoEquivalenciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResultadoEquivalenciaService {

    private final ResultadoEquivalenciaRepository resultadoEquivalenciaRepository;
    public ResultadoEquivalenciaService(ResultadoEquivalenciaRepository resultadoEquivalenciaRepository) {
        this.resultadoEquivalenciaRepository = resultadoEquivalenciaRepository;
    }

    public List<ResultadoEquivalenciaDTO> obtenerEquivalencias() {
        log.info("Obteniendo lista de equivalencias");
        List<ResultadoEquivalencia> lista = resultadoEquivalenciaRepository.findAll();
        List<ResultadoEquivalenciaDTO> dtoList = new ArrayList<>();
        for (ResultadoEquivalencia e : lista) {
            ResultadoEquivalenciaDTO dto = new ResultadoEquivalenciaDTO();
            dto.setIdEquivalencia(e.getIdEquivalencia());
            dto.setTipoEquivalencia(e.getTipoEquivalencia());
            dto.setValorEquivalente((double) e.getValorEquivalente());
            dto.setUnidad(e.getUnidad());
            if (e.getResultado() != null) dto.setResultadoId(e.getResultado().getIdResultado());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public ResultadoEquivalencia insertar(ResultadoEquivalenciaDTO dto) {
        log.info("Insertando equivalencia {}", dto.getTipoEquivalencia());
        ResultadoEquivalencia e = new ResultadoEquivalencia();
        e.setTipoEquivalencia(dto.getTipoEquivalencia());
        e.setValorEquivalente(dto.getValorEquivalente().floatValue());
        e.setUnidad(dto.getUnidad());

        if (dto.getResultadoId() != null) {
            Resultado r = new Resultado();
            r.setIdResultado(dto.getResultadoId());
            e.setResultado(r);
        }
        return resultadoEquivalenciaRepository.save(e);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando equivalencia con ID: {}", id);
        resultadoEquivalenciaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
