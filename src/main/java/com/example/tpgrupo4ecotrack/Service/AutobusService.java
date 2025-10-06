package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.AlimentoDTO;
import com.example.tpgrupo4ecotrack.DTO.AutobusDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.AutobusRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AutobusService {

    private final AutobusRepository autobusRepository;
    private final UsuarioRepository usuarioRepository;

    public AutobusService(AutobusRepository autobusRepository, UsuarioRepository usuarioRepository) {
        this.autobusRepository = autobusRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public AutobusDTO insertar(AutobusDTO dto) {
        log.info("Insertar Alimento: {}", dto.getIdAutobus());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaAutobus autobus = modelMapper.map(dto, SubCategoriaAutobus.class);

        // Relaciones antes de guardar
        if (dto.getUsuarioid() != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(dto.getUsuarioid());
            autobus.setUsuario(usuario);
        }

        if (dto.getCategoriaid() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(dto.getCategoriaid());
            autobus.setCategoria(categoria);
        }

        if (dto.getFactorid() != null) {
            FactorEmision factorEmision = new FactorEmision();
            factorEmision.setIdFactor(dto.getFactorid());
            autobus.setFactor(factorEmision);
        }

        autobus = autobusRepository.save(autobus);
        return modelMapper.map(autobus, AutobusDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return autobusRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando transporte público con ID: {}", id);
        autobusRepository.deleteById(id);
        return "Registro eliminado";
    }
}