package com.example.tpgrupo4ecotrack.Repository;


import com.example.tpgrupo4ecotrack.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}


