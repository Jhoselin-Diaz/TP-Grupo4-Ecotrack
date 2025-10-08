package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultadoDetalleRepository extends JpaRepository<ResultadoDetalle, Long> {

    List<ResultadoDetalle> findByResultado_IdResultado(Long resultadoId);
}
