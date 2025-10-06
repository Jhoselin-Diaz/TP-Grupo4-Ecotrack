package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.Categoria;
import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.AlimentoRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;

    public AlimentoService(AlimentoRepository alimentoRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository) {
        this.alimentoRepository = alimentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
    }

    public List<AlimentoDTO> listaAlimentos() {
        log.info("Obteniendo lista de Alimentos");
        List<SubCategoriaAlimento> alimentos = alimentoRepository.findAll();
        List<AlimentoDTO> alimentoDTO = new ArrayList<>();
        for (SubCategoriaAlimento alimento : alimentos) {
            AlimentoDTO dto = new AlimentoDTO();
            dto.setIdAlimento(alimento.getIdAlimento());
            dto.setNombreAlimento(alimento.getNombreAlimento());
            dto.setEmisionesKgCO2_AL(alimento.getEmisionesKgCO2_AL());
            dto.setCantidadKg(alimento.getCantidadKg());
            dto.setEnviadoResultadoAL(alimento.getEnviadoResultadoAL());
            dto.setFechaRegistro(alimento.getFechaRegistro());
            dto.setUsuarioid(alimento.getUsuario().getIdUsuario());
            dto.setCategoriaid(alimento.getCategoria().getIdCategoria());
            dto.setFactorid(alimento.getFactor().getIdFactor());
            alimentoDTO.add(dto);

        }
        return alimentoDTO;
    }

    public List<ListaAlimentoPorUsuarioDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaAlimento> lista = alimentoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(alimento -> modelMapper.map(alimento, ListaAlimentoPorUsuarioDTO.class))
                .toList();
    }

    public AlimentoDTO insertar(AlimentoDTO dto) {
        log.info("Insertar Alimento: {}", dto.getIdAlimento());
        ModelMapper modelMapper = new ModelMapper();
        SubCategoriaAlimento alimento = modelMapper.map(dto, SubCategoriaAlimento.class);

        // Relaciones antes de guardar
        if (dto.getUsuarioid() != null) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(dto.getUsuarioid());
            alimento.setUsuario(usuario);
        }

        if (dto.getCategoriaid() != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(dto.getCategoriaid());
            alimento.setCategoria(categoria);
        }

        if (dto.getFactorid() != null) {
            FactorEmision factorEmision = new FactorEmision();
            factorEmision.setIdFactor(dto.getFactorid());
            alimento.setFactor(factorEmision);
        }

        alimento = alimentoRepository.save(alimento);
        return modelMapper.map(alimento, AlimentoDTO.class);
    }

    public AlimentoDTO actualizar(Long id, AlimentoDTO alimentoDTO) {
        SubCategoriaAlimento alimento = alimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factor Emisión no encontrado"));

        if (alimentoDTO.getNombreAlimento() != null && !alimentoDTO.getNombreAlimento().isBlank())
            alimento.setNombreAlimento(alimentoDTO.getNombreAlimento());
        if (alimentoDTO.getEmisionesKgCO2_AL() != null &&
                !Float.isNaN(alimentoDTO.getEmisionesKgCO2_AL())) {
            alimento.setEmisionesKgCO2_AL(alimentoDTO.getEmisionesKgCO2_AL());
        }
        if (alimentoDTO.getCantidadKg() != null &&
                !Float.isNaN(alimentoDTO.getCantidadKg())) {
            alimento.setCantidadKg(alimentoDTO.getCantidadKg());
        }

        alimento.setFechaRegistro(LocalDateTime.now());


        alimentoRepository.save(alimento);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(alimento, AlimentoDTO.class);
    }


    public AlimentoDTO actualizarPorUser(Long id, AlimentoCreateDTO dto, String username) {
        SubCategoriaAlimento alimento = alimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

        // Validar que el usuario autenticado sea el dueño del registro
        if (!alimento.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este alimento");
        }

        // Solo actualiza los campos que el usuario quiera
        if (dto.getNombreAlimento() != null && !dto.getNombreAlimento().isBlank())
            alimento.setNombreAlimento(dto.getNombreAlimento());

        if (dto.getCantidadKg() != null && !Float.isNaN(dto.getCantidadKg()))
            alimento.setCantidadKg(dto.getCantidadKg());

        // Actualiza el factor automáticamente según el nombre (si cambió)
        String nombre = alimento.getNombreAlimento().toLowerCase();
        String codigoFactor;

        if (nombre.contains("pollo")) codigoFactor = "POLLO";
        else if (nombre.contains("carne") || nombre.contains("res")) codigoFactor = "CARNE_RES";
        else if (nombre.contains("cerdo")) codigoFactor = "CERDO";
        else if (nombre.contains("leche")) codigoFactor = "LACTEOS";
        else codigoFactor = "VEGETALES";

        FactorEmision factor = (FactorEmision) factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        alimento.setFactor(factor);
        alimento.setEmisionesKgCO2_AL(factor.getFactorKgCO2PorUnidad() * alimento.getCantidadKg());
        alimento.setFechaRegistro(LocalDateTime.now());

        alimentoRepository.save(alimento);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(alimento, AlimentoDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return alimentoRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }


    public String eliminar(Long id) {
        log.warn("Eliminando alimento con ID: {}", id);
        alimentoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
