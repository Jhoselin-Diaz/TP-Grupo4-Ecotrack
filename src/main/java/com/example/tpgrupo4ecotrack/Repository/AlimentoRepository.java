package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AlimentoRepository extends JpaRepository<SubCategoriaAlimento, Long> {

    List<SubCategoriaAlimento> findByUsuario_IdUsuario(Long usuarioId);

    @Query("SELECT SUM(a.emisionesKgCO2_AL) " +
            "FROM SubCategoriaAlimento a WHERE a.usuario.idUsuario = :usuarioId")
    Float sumEmisionesByUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT a FROM SubCategoriaAlimento a " +
            "WHERE a.usuario.idUsuario = :usuarioId " +
            "AND a.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<SubCategoriaAlimento> findByUsuarioAndFecha(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
}
