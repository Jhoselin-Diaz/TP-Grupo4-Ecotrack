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



    public void generarEquivalencias(Resultado resultado, float totalEmisiones) {
        // Ejemplo: 1 árbol absorbe ~21 kg CO2 al año
        float arboles = totalEmisiones / 21f;

        ResultadoEquivalencia eq1 = new ResultadoEquivalencia();
        eq1.setResultado(resultado);
        eq1.setTipoEquivalencia("Árboles necesarios");
        eq1.setValorEquivalente(arboles);
        eq1.setUnidad("árboles");

        resultadoEquivalenciaRepository.save(eq1);
    }


    public String eliminar(Long id) {
        log.warn("Eliminando equivalencia con ID: {}", id);
        resultadoEquivalenciaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
