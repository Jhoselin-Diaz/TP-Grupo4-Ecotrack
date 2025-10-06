package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDTO;
import com.example.tpgrupo4ecotrack.Entity.Categoria;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Entity.Resultado;
import com.example.tpgrupo4ecotrack.Repository.ResultadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    public ResultadoService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    public List<ResultadoDTO> obtenerResultados() {
        log.info("Obteniendo lista de resultados");
        List<Resultado> lista = resultadoRepository.findAll();
        List<ResultadoDTO> dtoList = new ArrayList<>();
        for (Resultado r : lista) {
            ResultadoDTO dto = new ResultadoDTO();
            dto.setIdResultado(r.getIdResultado());
            if (r.getHuella() != null) dto.setHuellaId(r.getHuella().getIdHuella());
            if (r.getCategoria() != null) dto.setCategoriaId(r.getCategoria().getIdCategoria());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public Resultado insertar(ResultadoDTO dto) {
        log.info("Insertando resultado");
        Resultado r = new Resultado();

        if (dto.getHuellaId() != null) {
            HuellaCarbono h = new HuellaCarbono();
            h.setIdHuella(dto.getHuellaId());
            r.setHuella(h);
        }
        if (dto.getCategoriaId() != null) {
            Categoria c = new Categoria();
            c.setIdCategoria(dto.getCategoriaId());
            r.setCategoria(c);
        }

        return resultadoRepository.save(r);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando resultado con ID: {}", id);
        resultadoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
