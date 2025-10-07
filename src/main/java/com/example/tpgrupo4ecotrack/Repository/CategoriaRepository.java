package com.example.tpgrupo4ecotrack.Repository;

import com.example.tpgrupo4ecotrack.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {


    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombreCategoria) = LOWER(:nombre)")
    Optional<Categoria> findByNombreCategoriaIgnoreCase(
            @Param("nombre") String nombre);
}

