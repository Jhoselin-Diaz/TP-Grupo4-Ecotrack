package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FactorEmisionRepository extends JpaRepository<FactorEmision, Long> {
    Optional<FactorEmision> findByCodigoIgnoreCase(String codigoFactor);

    @Query("SELECT f " +
            "FROM FactorEmision f " +
            "WHERE UPPER(f.codigo) = UPPER(:codigo)")
    Optional<FactorEmision> findByCodigoIgnoreCaseInsert(
            @Param("codigo") String codigo);
}
