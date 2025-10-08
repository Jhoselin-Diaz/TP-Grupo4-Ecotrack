package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAutobus;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaCoche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface SAutobusRepository extends JpaRepository<SubCategoriaAutobus, Long> {

    List<SubCategoriaAutobus> findByUsuario_IdUsuario(Long usuarioId);

    @Query("SELECT SUM(a.emisionesKgCO2_A) " +
            "FROM SubCategoriaAutobus a WHERE a.usuario.idUsuario = :usuarioId")
    Float sumEmisionesByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM SubCategoriaAutobus a " +
            "WHERE a.usuario.idUsuario = :usuarioId " +
            "AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<SubCategoriaAutobus> findByUsuarioAndFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
