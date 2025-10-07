package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.SAutobusDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SAutobusRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SAutobusService {

    private final SAutobusRepository SautobusRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SAutobusService(SAutobusRepository SautobusRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaRepository categoriaRepository ) {
        this.SautobusRepository = SautobusRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }


    public SAutobusDTO insertar(SAutobusDTO dto) {
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

        autobus = SautobusRepository.save(autobus);
        return modelMapper.map(autobus, SAutobusDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return SautobusRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando transporte público con ID: {}", id);
        SautobusRepository.deleteById(id);
        return "Registro eliminado";
    }
}