package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.AlimentoDTO;
import com.example.tpgrupo4ecotrack.DTO.ListaAlimentoPorUsuarioDTO;
import com.example.tpgrupo4ecotrack.DTO.ListarServicioDTO;
import com.example.tpgrupo4ecotrack.DTO.ServicioViviendaDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.ServicioViviendaRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ServicioViviendaService {

    private final ServicioViviendaRepository servicioViviendaRepository;
    private final UsuarioRepository usuarioRepository;

    public ServicioViviendaService(ServicioViviendaRepository servicioViviendaRepository, UsuarioRepository usuarioRepository) {
        this.servicioViviendaRepository = servicioViviendaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ServicioViviendaDTO insertar(ServicioViviendaDTO dto) {
        log.info("Insertar Servicio: {}", dto.getIdServicios());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaServicioVivienda servicio = modelMapper.map(dto, SubCategoriaServicioVivienda.class);

        // Relaciones antes de guardar
        if (dto.getUsuarioid() != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(dto.getUsuarioid());
            servicio.setUsuario(usuario);
        }

        if (dto.getCategoriaid() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(dto.getCategoriaid());
            servicio.setCategoria(categoria);
        }

        if (dto.getFactorid() != null) {
            FactorEmision factorEmision = new FactorEmision();
            factorEmision.setIdFactor(dto.getFactorid());
            servicio.setFactor(factorEmision);
        }

        servicio = servicioViviendaRepository.save(servicio);
        return modelMapper.map(servicio, ServicioViviendaDTO.class);
    }

    public List<ListarServicioDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaServicioVivienda> lista = servicioViviendaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(servicio -> modelMapper.map(servicio, ListarServicioDTO.class))
                .toList();
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return servicioViviendaRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando servicio de vivienda con ID: {}", id);
        servicioViviendaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
