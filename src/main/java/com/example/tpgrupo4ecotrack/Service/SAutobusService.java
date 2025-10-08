package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SAutobusRepository;
import com.example.tpgrupo4ecotrack.Repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
public class SAutobusService {

    private final SAutobusRepository sautobusRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SAutobusService(SAutobusRepository sautobusRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaRepository categoriaRepository ) {
        this.sautobusRepository = sautobusRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SAutobusDTO> listaAutobusAdmin() {
        log.info("Obteniendo lista de Autobus");
        List<SubCategoriaAutobus> autobus = sautobusRepository.findAll();
        List<SAutobusDTO> sAutobusDTO = new ArrayList<>();

        // Recorre cada objeto coche obtenido del repositorio
        for (SubCategoriaAutobus auto : autobus) {
            SAutobusDTO dto = new SAutobusDTO();
            dto.setIdAutobus(auto.getIdAutobus());
            dto.setAutobusKm(auto.getAutobusKm());
            dto.setAutocarKm(auto.getAutocarKm());
            dto.setMetroKm(auto.getMetroKm());
            dto.setTranviaKm(auto.getTranviaKm());
            dto.setTaxiKm(auto.getTaxiKm());
            dto.setTrenNacionalKm(auto.getTrenNacionalKm());
            dto.setEmisionesKgCO2_A(auto.getEmisionesKgCO2_A());
            dto.setEnviadoResultadoA(auto.getEnviadoResultadoA());
            dto.setFechaRegistro(auto.getFechaRegistro());
            dto.setUsuarioid(auto.getUsuario().getIdUsuario());
            dto.setCategoriaid(auto.getCategoria().getIdCategoria());
            dto.setFactorid(auto.getFactor().getIdFactor());
            sAutobusDTO.add(dto);
        }
        return sAutobusDTO;
    }

    public List<ListarAutobusDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaAutobus> lista = sautobusRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(autobus -> modelMapper.map(autobus, ListarAutobusDTO.class))
                .toList();
    }

    public SAutobusDTO Registrar(SAutobusCreateDTO dto, String username) {


        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String codigoFactor = detectarCodigoFactor(dto.getTipoGasolina());
        if (codigoFactor == null)
            throw new RuntimeException("No se pudo detectar el tipo de combustible: " + dto.getTipoGasolina());

        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Autobús")
                .orElseThrow(() -> new RuntimeException("Categoría 'Autobús' no encontrada"));

        Float km = dto.getAutobusKm() != null ? dto.getAutobusKm() : 0f;
        Float emisiones = factor.getFactorKgCO2PorUnidad() * km;

        SubCategoriaAutobus autobus = new SubCategoriaAutobus();
        autobus.setTipoGasolina(dto.getTipoGasolina());
        autobus.setAutobusKm(dto.getAutobusKm());
        autobus.setAutocarKm(dto.getAutocarKm());
        autobus.setTaxiKm(dto.getTaxiKm());
        autobus.setMetroKm(dto.getMetroKm());
        autobus.setTranviaKm(dto.getTranviaKm());
        autobus.setTrenNacionalKm(dto.getTrenNacionalKm());
        autobus.setEmisionesKgCO2_A(emisiones);
        autobus.setEnviadoResultadoA(true);
        autobus.setFechaRegistro(LocalDateTime.now());
        autobus.setUsuario(usuario);
        autobus.setCategoria(categoria);
        autobus.setFactor(factor);

        sautobusRepository.save(autobus);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(autobus, SAutobusDTO.class);
    }

    private String detectarCodigoFactor(String tipoGasolina) {
        String nombre = tipoGasolina.toLowerCase();
        if (nombre.contains("gas licuado") || nombre.contains("licuado") || nombre.contains("petroleo") || nombre.contains("glp"))
            return "GLP";
        else if (nombre.contains("gas natural") || nombre.contains("gnv"))
            return "GAS_NAT";
        else if (nombre.contains("nafta"))
            return "NAFTA";
        else if (nombre.contains("diesel"))
            return "AUTO_DIESEL";
        else if (nombre.contains("gasolina"))
            return "AUTO_PETROL";
        else
            return null;
    }

    public SAutobusDTO actualizar(Long id, SAutobusCreateDTO dto, String username) {
        SubCategoriaAutobus autobus = sautobusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autobús no encontrado"));

        // Valida que el registro pertenece al usuario autenticado
        if (!autobus.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este registro");
        }

        // Actualiza solo los campos que el usuario haya enviado
        if (dto.getAutobusKm() != null) autobus.setAutobusKm(dto.getAutobusKm());
        if (dto.getAutocarKm() != null) autobus.setAutocarKm(dto.getAutocarKm());
        if (dto.getMetroKm() != null) autobus.setMetroKm(dto.getMetroKm());
        if (dto.getTaxiKm() != null) autobus.setTaxiKm(dto.getTaxiKm());
        if (dto.getTranviaKm() != null) autobus.setTranviaKm(dto.getTranviaKm());
        if (dto.getTrenNacionalKm() != null) autobus.setTrenNacionalKm(dto.getTrenNacionalKm());
        if (dto.getTipoGasolina() != null && !dto.getTipoGasolina().isBlank()) {
            autobus.setTipoGasolina(dto.getTipoGasolina());
        }

        // Detecta el factor según el tipo de gasolina actual
        String codigoFactor = detectarCodigoFactor(autobus.getTipoGasolina());
        if (codigoFactor == null)
            throw new RuntimeException("No se pudo detectar el tipo de combustible: " + autobus.getTipoGasolina());

        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        // Recalcula emisiones
        float totalKm =
                (autobus.getAutobusKm() != null ? autobus.getAutobusKm() : 0f) +
                        (autobus.getAutocarKm() != null ? autobus.getAutocarKm() : 0f) +
                        (autobus.getTaxiKm() != null ? autobus.getTaxiKm() : 0f) +
                        (autobus.getMetroKm() != null ? autobus.getMetroKm() : 0f) +
                        (autobus.getTranviaKm() != null ? autobus.getTranviaKm() : 0f) +
                        (autobus.getTrenNacionalKm() != null ? autobus.getTrenNacionalKm() : 0f);

        float emisiones = factor.getFactorKgCO2PorUnidad() * totalKm;

        autobus.setFactor(factor);
        autobus.setEmisionesKgCO2_A(emisiones);
        autobus.setFechaRegistro(LocalDateTime.now());

        sautobusRepository.save(autobus);

        // Mapear a DTO
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(autobus, SAutobusDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuario() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return sautobusRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando transporte público con ID: {}", id);
        sautobusRepository.deleteById(id);
        return "Registro eliminado";
    }
}