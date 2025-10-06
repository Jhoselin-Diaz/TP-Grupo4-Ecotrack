package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AutobusRepository extends JpaRepository<SubCategoriaAutobus, Long> {

    @Query("SELECT SUM(a.emisionesKgCO2_A) " +
            "FROM SubCategoriaAutobus a WHERE a.usuario.idUsuario = :usuarioId")
    Float sumEmisionesByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM SubCategoriaAutobus a " +
            "WHERE a.usuario.idUsuario = :usuarioId " +
            "AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<SubCategoriaAutobus> findByUsuarioAndFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}
