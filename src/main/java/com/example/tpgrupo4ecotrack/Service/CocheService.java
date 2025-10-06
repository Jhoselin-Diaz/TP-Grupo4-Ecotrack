package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.AlimentoDTO;
import com.example.tpgrupo4ecotrack.DTO.CocheDTO;
import com.example.tpgrupo4ecotrack.DTO.ListaAlimentoPorUsuarioDTO;
import com.example.tpgrupo4ecotrack.DTO.ListarCocheDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CocheRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CocheService {

    private final CocheRepository cocheRepository;
    private final UsuarioRepository usuarioRepository;

    public CocheService(CocheRepository cocheRepository, UsuarioRepository usuarioRepository) {

        this.cocheRepository = cocheRepository;
        this. usuarioRepository = usuarioRepository;
    }


    public CocheDTO insertar(CocheDTO dto) {
        log.info("Insertar Alimento: {}", dto.getMarca());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaCoche coche = modelMapper.map(dto, SubCategoriaCoche.class);

        // Relaciones antes de guardar
        if (dto.getUsuarioid() != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(dto.getUsuarioid());
            coche.setUsuario(usuario);
        }

        if (dto.getCategoriaid() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(dto.getCategoriaid());
            coche.setCategoria(categoria);
        }

        if (dto.getFactorid() != null) {
            FactorEmision factorEmision = new FactorEmision();
            factorEmision.setIdFactor(dto.getFactorid());
            coche.setFactor(factorEmision);
        }

        coche = cocheRepository.save(coche);
        return modelMapper.map(coche, CocheDTO.class);
    }

    public List<ListarCocheDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaCoche> lista = cocheRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(coche -> modelMapper.map(coche, ListarCocheDTO.class))
                .toList();
    }

    public Long getTotalEmisionesCoche(Long usuarioId) {
        return cocheRepository.getTotalEmisionesByUsuario(usuarioId);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando coche con ID: {}", id);
        cocheRepository.deleteById(id);
        return "Registro eliminado";
    }
}