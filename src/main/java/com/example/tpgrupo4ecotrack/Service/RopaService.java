package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.AlimentoDTO;
import com.example.tpgrupo4ecotrack.DTO.ListaAlimentoPorUsuarioDTO;
import com.example.tpgrupo4ecotrack.DTO.ListaRopaDTO;
import com.example.tpgrupo4ecotrack.DTO.RopaDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.RopaRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RopaService {

    private final RopaRepository ropaRepository;
    private final UsuarioRepository usuarioRepository;

    public RopaService(RopaRepository ropaRepository, UsuarioRepository usuarioRepository) {

        this.ropaRepository = ropaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public RopaDTO insertar(RopaDTO dto) {
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

        ropa = ropaRepository.save(ropa);
        return modelMapper.map(ropa, RopaDTO.class);
    }

    public List<ListaRopaDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaRopa> lista = ropaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(ropa -> modelMapper.map(ropa, ListaRopaDTO.class))
                .toList();
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ropaRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando prenda con ID: {}", id);
        ropaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
