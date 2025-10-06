package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactorEmisionRepository extends JpaRepository<FactorEmision, Long> {
    Optional<Object> findByCodigoIgnoreCase(String codigoFactor);
}
