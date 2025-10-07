package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.SubCategoriaCoche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SCocheRepository extends JpaRepository<SubCategoriaCoche, Long> {

    List<SubCategoriaCoche> findByUsuario_IdUsuario(Long usuarioId);

    @Query("SELECT SUM(a.emisionesKgCO2_C) " +
            "FROM SubCategoriaCoche a WHERE a.usuario.idUsuario = :usuarioId")
    Float sumEmisionesByUsuario(
            @Param("usuarioId") Long usuarioId);


    @Query("SELECT a FROM SubCategoriaCoche a " +
            "WHERE a.usuario.idUsuario = :usuarioId " +
            "AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<SubCategoriaCoche> findByUsuarioAndFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);

}
