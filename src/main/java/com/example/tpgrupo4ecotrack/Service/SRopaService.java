package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SRopaRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<SRopaDTO> listaRopaAdmin() {
        log.info("Obteniendo lista de Ropa");
        List<SubCategoriaRopa> ropas = SropaRepository.findAll();
        List<SRopaDTO> sRopaDTO = new ArrayList<>();

        // Recorre cada objeto coche obtenido del repositorio
        for (SubCategoriaRopa ropa : ropas) {
            SRopaDTO dto = new SRopaDTO();
            dto.setIdRopa(ropa.getIdRopa());
            dto.setTipoPrenda(ropa.getTipoPrenda());
            dto.setCantidadKg(ropa.getCantidadKg());
            dto.setEmisionesKgCO2_R(ropa.getEmisionesKgCO2_R());
            dto.setEnviadoResultadoR(ropa.getEnviadoResultadoR());
            dto.setFechaRegistro(ropa.getFechaRegistro());
            dto.setUsuarioid(ropa.getUsuario().getIdUsuario());
            dto.setCategoriaid(ropa.getCategoria().getIdCategoria());
            dto.setFactorid(ropa.getFactor().getIdFactor());
            sRopaDTO.add(dto);
        }
        return sRopaDTO;
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

    public SRopaDTO Registrar(SRopaCreateDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Detecta automáticamente el código del factor según el nombre
        String nombre = dto.getTipoPrenda().toLowerCase();
        String codigoFactor;

        if (nombre.contains("polo") || nombre.contains("camisa") || nombre.contains("casaca") ||
                nombre.contains("sueter") || nombre.contains("sudadera")) codigoFactor = "CAMISETA";
        else if (nombre.contains("short") || nombre.contains("jean") || nombre.contains("jogger")
                || nombre.contains("licra")) codigoFactor = "PANTALON";
        else codigoFactor = "";

        // Busca el factor según el código
        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        // Busca la categoría
        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Ropa")
                .orElseThrow(() -> new RuntimeException("Categoría 'Ropa' no encontrada"));

        // Calcula las emisiones
        Float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getCantidadKg();

        // Crea entidad
        SubCategoriaRopa ropa = new SubCategoriaRopa();
        ropa.setTipoPrenda(dto.getTipoPrenda());
        ropa.setCantidadKg(dto.getCantidadKg());
        ropa.setEmisionesKgCO2_R(emisiones);
        ropa.setEnviadoResultadoR(false);
        ropa.setFechaRegistro(LocalDateTime.now());
        ropa.setUsuario(usuario);
        ropa.setCategoria(categoria);
        ropa.setFactor(factor);

        SropaRepository.save(ropa);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(ropa, SRopaDTO.class);
    }

    public SRopaDTO actualizar(Long id, SRopaCreateDTO dto, String username) {
        SubCategoriaRopa ropa = SropaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ropa no encontrada"));

        // Validar que el usuario autenticado sea el dueño del registro
        if (!ropa.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar esta ropa");
        }

        // Solo actualiza los campos que el usuario quiera
        if (dto.getTipoPrenda() != null && !dto.getTipoPrenda().isBlank())
            ropa.setTipoPrenda(dto.getTipoPrenda());
        if (dto.getCantidadKg() != null && !Float.isNaN(dto.getCantidadKg()))
            ropa.setCantidadKg(dto.getCantidadKg());

        // Actualiza el factor automáticamente según el nombre
        String nombre = ropa.getTipoPrenda().toLowerCase();
        String codigoFactor;

        if (nombre.contains("polo") || nombre.contains("camisa") || nombre.contains("casaca") ||
                nombre.contains("sueter") || nombre.contains("sudadera")) codigoFactor = "CAMISETA";
        else if (nombre.contains("short") || nombre.contains("jean") || nombre.contains("jogger")
                || nombre.contains("licra")) codigoFactor = "PANTALON";
        else codigoFactor = "";

        FactorEmision factor = (FactorEmision) factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        ropa.setFactor(factor);
        ropa.setEmisionesKgCO2_R(factor.getFactorKgCO2PorUnidad() * ropa.getCantidadKg());
        ropa.setFechaRegistro(LocalDateTime.now());

        SropaRepository.save(ropa);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(ropa, SRopaDTO.class);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando prenda con ID: {}", id);
        SropaRepository.deleteById(id);
        return "Registro eliminado";
    }
}
