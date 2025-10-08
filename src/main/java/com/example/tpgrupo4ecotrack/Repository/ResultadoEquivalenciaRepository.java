package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.ResultadoEquivalencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoEquivalenciaRepository extends JpaRepository<ResultadoEquivalencia, Long> {
    List<ResultadoEquivalencia> findByResultado_IdResultado(Long resultadoId);
}
