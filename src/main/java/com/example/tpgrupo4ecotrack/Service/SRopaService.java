package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ListaRopaDTO;
import com.example.tpgrupo4ecotrack.DTO.SRopaDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SRopaRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SRopaService {

    private final SRopaRepository SropaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SRopaService(SRopaRepository SropaRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaRepository categoriaRepository) {

        this.SropaRepository = SropaRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }


    public SRopaDTO insertar(SRopaDTO dto) {
        log.info("Insertar Ropa: {}", dto.getTipoPrenda());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaRopa ropa = modelMapper.map(dto, SubCategoriaRopa.class);

        // Relaciones antes de guardar
        if (dto.getUsuarioid() != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(dto.getUsuarioid());
            ropa.setUsuario(usuario);
        }

        if (dto.getCategoriaid() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(dto.getCategoriaid());
            ropa.setCategoria(categoria);
        }

        if (dto.getFactorid() != null) {
            FactorEmision factorEmision = new FactorEmision();
            factorEmision.setIdFactor(dto.getFactorid());
            ropa.setFactor(factorEmision);
        }

        ropa = SropaRepository.save(ropa);
        return modelMapper.map(ropa, SRopaDTO.class);
    }

    public List<ListaRopaDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaRopa> lista = SropaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(ropa -> modelMapper.map(ropa, ListaRopaDTO.class))
                .toList();
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return SropaRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando prenda con ID: {}", id);
        SropaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
