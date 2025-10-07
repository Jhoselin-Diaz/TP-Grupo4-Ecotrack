package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ListarServicioDTO;
import com.example.tpgrupo4ecotrack.DTO.SServicioViviendaDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SServicioViviendaRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SServicioViviendaService {

    private final SServicioViviendaRepository SservicioViviendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;


    public SServicioViviendaService(SServicioViviendaRepository SservicioViviendaRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaRepository categoriaRepository) {
        this.SservicioViviendaRepository = SservicioViviendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public SServicioViviendaDTO insertar(SServicioViviendaDTO dto) {
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

        servicio = SservicioViviendaRepository.save(servicio);
        return modelMapper.map(servicio, SServicioViviendaDTO.class);
    }

    public List<ListarServicioDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaServicioVivienda> lista = SservicioViviendaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(servicio -> modelMapper.map(servicio, ListarServicioDTO.class))
                .toList();
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return SservicioViviendaRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando servicio de vivienda con ID: {}", id);
        SservicioViviendaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
