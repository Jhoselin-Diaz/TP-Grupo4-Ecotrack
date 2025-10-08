package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SElectrodomesticoRepository;
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
public class SElectrodomesticoService {

    private final SElectrodomesticoRepository SelectrodomesticoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SElectrodomesticoService(SElectrodomesticoRepository SelectrodomesticoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository,  FactorEmisionRepository factorEmisionRepository) {
        this.SelectrodomesticoRepository = SelectrodomesticoRepository;
        this. usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SElectrodomesticoDTO> listaElectrodomesticoAdmin() {
        log.info("Obteniendo lista de Electrodomestico");
        List<SubCategoriaElectrodomestico> electrodomesticos = SelectrodomesticoRepository.findAll();
        List<SElectrodomesticoDTO> sElectrodomesticoDTO = new ArrayList<>();

        // Recorre cada objeto coche obtenido del repositorio
        for (SubCategoriaElectrodomestico electrodomestico : electrodomesticos) {
            SElectrodomesticoDTO dto = new SElectrodomesticoDTO();
            dto.setIdElectrodomestico(electrodomestico.getIdElectrodomestico());
            dto.setTipoElectrodomestico(electrodomestico.getTipoElectrodomestico());
            dto.setConsumoKWh(electrodomestico.getConsumoKWh());
            dto.setEmisionesKgCO2_E(electrodomestico.getEmisionesKgCO2_E());
            dto.setEnviadoResultadoE(electrodomestico.getEnviadoResultadoE());
            dto.setFechaRegistro(electrodomestico.getFechaRegistro());
            dto.setUsuarioid(electrodomestico.getUsuario().getIdUsuario());
            dto.setCategoriaid(electrodomestico.getCategoria().getIdCategoria());
            dto.setFactorid(electrodomestico.getFactor().getIdFactor());
            sElectrodomesticoDTO.add(dto);
        }
        return sElectrodomesticoDTO;
    }

    public List<ListarElectDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaElectrodomestico> lista = SelectrodomesticoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(electrodomestico -> modelMapper.map(electrodomestico, ListarElectDTO.class))
                .toList();
    }

    public SElectrodomesticoDTO Registrar(SElectCreateDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Detecta automáticamente el código del factor según el nombre
        String nombre = dto.getTipoElectrodomestico().toLowerCase();
        String codigoFactor;

        if (nombre.contains("refrigerador")) codigoFactor = "REFRIGERADOR";
        else if (nombre.contains("lavadora")) codigoFactor = "LAVADORA";
        else codigoFactor = "";

        // Busca el factor según el código
        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        // Busca la categoría
        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Electrodomésticos")
                .orElseThrow(() -> new RuntimeException("Categoría 'Electrodomestico' no encontrada"));

        // Calcula las emisiones
        Float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getConsumoKWh();

        // Crea entidad
        SubCategoriaElectrodomestico electrodomestico = new SubCategoriaElectrodomestico();
        electrodomestico.setTipoElectrodomestico(dto.getTipoElectrodomestico());
        electrodomestico.setConsumoKWh(dto.getConsumoKWh());
        electrodomestico.setEmisionesKgCO2_E(emisiones);
        electrodomestico.setEnviadoResultadoE(false);
        electrodomestico.setFechaRegistro(LocalDateTime.now());
        electrodomestico.setUsuario(usuario);
        electrodomestico.setCategoria(categoria);
        electrodomestico.setFactor(factor);

        SelectrodomesticoRepository.save(electrodomestico);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(electrodomestico, SElectrodomesticoDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return SelectrodomesticoRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public SElectrodomesticoDTO actualizar(Long id, SElectCreateDTO dto, String username) {
        SubCategoriaElectrodomestico electrodomestico = SelectrodomesticoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Electrodomestico no encontrado"));

        // Validar que el usuario autenticado sea el dueño del registro
        if (!electrodomestico.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este electrodomestico");
        }

        // Solo actualiza los campos que el usuario quiera
        if (dto.getTipoElectrodomestico() != null && !dto.getTipoElectrodomestico().isBlank())
            electrodomestico.setTipoElectrodomestico(dto.getTipoElectrodomestico());
        if (dto.getConsumoKWh() != null && !Float.isNaN(dto.getConsumoKWh()))
            electrodomestico.setConsumoKWh(dto.getConsumoKWh());

        // Actualiza el factor automáticamente según el nombre
        String nombre = electrodomestico.getTipoElectrodomestico().toLowerCase();
        String codigoFactor;

        if (nombre.contains("refrigerador")) codigoFactor = "REFRIGERADOR";
        else if (nombre.contains("lavadora")) codigoFactor = "LAVADORA";
        else codigoFactor = "";

        FactorEmision factor = (FactorEmision) factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        electrodomestico.setFactor(factor);
        electrodomestico.setEmisionesKgCO2_E(factor.getFactorKgCO2PorUnidad() * electrodomestico.getConsumoKWh());
        electrodomestico.setFechaRegistro(LocalDateTime.now());

        SelectrodomesticoRepository.save(electrodomestico);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(electrodomestico, SElectrodomesticoDTO.class);
    }

    public String eliminar(Long id) {
        log.warn("Eliminando electrodoméstico con ID: {}", id);
        SelectrodomesticoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
