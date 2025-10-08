package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.SubCategoriaCoche;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaElectrodomestico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SElectrodomesticoRepository extends JpaRepository<SubCategoriaElectrodomestico, Long> {

    List<SubCategoriaElectrodomestico> findByUsuario_IdUsuario(Long usuarioId);

    @Query("SELECT SUM(e.emisionesKgCO2_E) FROM SubCategoriaElectrodomestico e " +
            "WHERE e.usuario.idUsuario = :usuarioId")
    Float sumEmisionesByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM SubCategoriaElectrodomestico a " +
            "WHERE a.usuario.idUsuario = :usuarioId " +
            "AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<SubCategoriaElectrodomestico> findByUsuarioAndFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);

}
