package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaElectrodomestico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HuellaCarbonoRepository extends JpaRepository<HuellaCarbono, Long> {

    List<HuellaCarbono> findByUsuario_IdUsuario(Long usuarioId);

    @Query("SELECT new com.example.tpgrupo4ecotrack.DTO.HuellaDTO(h.fechaCalculo, h.usuario.idUsuario) " +
            "FROM HuellaCarbono h " +
            "WHERE h.usuario.idUsuario = :usuarioID " +
            "AND h.fechaCalculo BETWEEN :anioInicio AND :anioFin " +
            "ORDER BY h.fechaCalculo ASC")
    List<HuellaDTO> EncontrarEntreAnio(
            @Param("usuarioID") Long usuarioID,
            @Param("anioInicio") LocalDate anioInicio,
            @Param("anioFin") LocalDate anioFin);


}


