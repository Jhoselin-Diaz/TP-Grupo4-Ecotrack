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

    public List<SAutobusDTO> listaAutobusAdmin() {
        log.info("Obteniendo lista de Autobus");
        List<SubCategoriaAutobus> autobus = SautobusRepository.findAll();
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

        List<SubCategoriaAutobus> lista = SautobusRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(autobus -> modelMapper.map(autobus, ListarAutobusDTO.class))
                .toList();
    }

    public SAutobusDTO Registrar(SAutobusCreateDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Detecta automáticamente el código del factor según el nombre
        String nombre = dto.getTipoGasolina().toLowerCase();
        String codigoFactor;

        if (nombre.contains("gas licuado") || nombre.contains("licuado") || nombre.contains("petroleo") ||
                nombre.contains("glp")) codigoFactor = "GLP";
        else if (nombre.contains("gas natural") || nombre.contains("gnv")) codigoFactor = "GAS_NAT";
        else if (nombre.contains("nafta ")) codigoFactor = "NAFTA";
        else if (nombre.contains("diesel")) codigoFactor = "AUTO_DIESEL";
        else if (nombre.contains("gasolina")) codigoFactor = "AUTO_PETROL";
        else codigoFactor = "";

        // Busca el factor según el código
        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        // Busca la categoría
        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Autobus")
                .orElseThrow(() -> new RuntimeException("Categoría 'Autobus' no encontrada"));

        // Calcula las emisiones
        Float emisiones = factor.getFactorKgCO2PorUnidad() * (dto.getAutobusKm() + dto.getTaxiKm()
                + dto.getAutocarKm() + dto.getMetroKm() + dto.getTranviaKm() + dto.getTrenNacionalKm());

        // Crea entidad
        SubCategoriaAutobus autobus = new SubCategoriaAutobus();
        autobus.setTipoGasolina(dto.getTipoGasolina());
        autobus.setAutobusKm(dto.getAutobusKm());
        autobus.setAutocarKm(dto.getAutocarKm());
        autobus.setTaxiKm(dto.getTaxiKm());
        autobus.setMetroKm(dto.getMetroKm());
        autobus.setTranviaKm(dto.getTranviaKm());
        autobus.setTrenNacionalKm(dto.getTrenNacionalKm());
        autobus.setEmisionesKgCO2_A(emisiones);
        autobus.setEnviadoResultadoA(false);
        autobus.setFechaRegistro(LocalDateTime.now());
        autobus.setUsuario(usuario);
        autobus.setCategoria(categoria);
        autobus.setFactor(factor);

        SautobusRepository.save(autobus);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(autobus, SAutobusDTO.class);
    }

    public SAutobusDTO actualizar(Long id, SAutobusCreateDTO dto, String username) {
        SubCategoriaAutobus autobus = SautobusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autobus no encontrado"));

        // Validar que el usuario autenticado sea el dueño del registro
        if (!autobus.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este auto");
        }

        // Solo actualiza los campos que el usuario quiera
        if (dto.getAutobusKm() != null && !Float.isNaN(dto.getAutobusKm()))
            autobus.setAutobusKm(dto.getAutobusKm());
        if (dto.getAutocarKm() != null && !Float.isNaN(dto.getAutocarKm()))
            autobus.setAutocarKm(dto.getAutocarKm());
        if (dto.getMetroKm() != null && !Float.isNaN(dto.getMetroKm()))
            autobus.setMetroKm(dto.getMetroKm());
        if (dto.getTaxiKm() != null && !Float.isNaN(dto.getTaxiKm()))
            autobus.setTaxiKm(dto.getTaxiKm());
        if (dto.getTranviaKm() != null && !Float.isNaN(dto.getTranviaKm()))
            autobus.setTranviaKm(dto.getTranviaKm());
        if (dto.getTrenNacionalKm() != null && !Float.isNaN(dto.getTrenNacionalKm()))
            autobus.setTrenNacionalKm(dto.getTrenNacionalKm());
        if (dto.getTipoGasolina() != null && !dto.getTipoGasolina().isBlank())
            autobus.setTipoGasolina(dto.getTipoGasolina());

        // Actualiza el factor automáticamente según el nombre
        String nombre = autobus.getTipoGasolina().toLowerCase();
        String codigoFactor;

        if (nombre.contains("gas licuado") || nombre.contains("licuado") || nombre.contains("petroleo") ||
                nombre.contains("glp")) codigoFactor = "GLP";
        else if (nombre.contains("gas natural") || nombre.contains("gnv")) codigoFactor = "GAS_NAT";
        else if (nombre.contains("nafta ")) codigoFactor = "NAFTA";
        else if (nombre.contains("diesel")) codigoFactor = "AUTO_DIESEL";
        else if (nombre.contains("gasolina")) codigoFactor = "AUTO_PETROL";
        else codigoFactor = "";

        FactorEmision factor = (FactorEmision) factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        autobus.setFactor(factor);
        autobus.setEmisionesKgCO2_A(factor.getFactorKgCO2PorUnidad() * (autobus.getAutobusKm() + autobus.getTaxiKm()
        + autobus.getAutocarKm() + autobus.getMetroKm() + autobus.getTranviaKm() + autobus.getTrenNacionalKm()));
        autobus.setFechaRegistro(LocalDateTime.now());

        SautobusRepository.save(autobus);

        ModelMapper modelMapper = new ModelMapper();
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