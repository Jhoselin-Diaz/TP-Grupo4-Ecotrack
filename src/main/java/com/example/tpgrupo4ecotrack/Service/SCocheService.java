package com.example.tpgrupo4ecotrack.Service;


import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.SCocheRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
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
public class SCocheService {

    private final SCocheRepository ScocheRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;

    public SCocheService( SCocheRepository ScocheRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaRepository categoriaRepository) {
        this.ScocheRepository = ScocheRepository;
        this. usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SCocheDTO> listaCocheAdmin() {
        log.info("Obteniendo lista de Coche");
        List<SubCategoriaCoche> coches = ScocheRepository.findAll();
        List<SCocheDTO> sCocheDTO = new ArrayList<>();

        // Recorre cada objeto coche obtenido del repositorio
        for (SubCategoriaCoche coche : coches) {
            SCocheDTO dto = new SCocheDTO();
            dto.setIdCoche(coche.getIdCoche());
            dto.setMarca(coche.getMarca());
            dto.setTipoGasolina(coche.getTipoGasolina());
            dto.setNumeroCoches(coche.getNumeroCoches());
            dto.setEmisionesKgCO2_C(coche.getEmisionesKgCO2_C());
            dto.setEnviadoResultadoC(coche.getEnviadoResultadoC());
            dto.setFechaRegistro(coche.getFechaRegistro());
            dto.setUsuarioid(coche.getUsuario().getIdUsuario());
            dto.setCategoriaid(coche.getCategoria().getIdCategoria());
            dto.setFactorid(coche.getFactor().getIdFactor());
            sCocheDTO.add(dto);
        }
        return sCocheDTO;
    }

    public List<ListarCocheDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaCoche> lista = ScocheRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(coche -> modelMapper.map(coche, ListarCocheDTO.class))
                .toList();
    }

    public SCocheDTO Registrar(SCocheCreateDTO dto, String username) {

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
        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Coche")
                .orElseThrow(() -> new RuntimeException("Categoría 'Coche' no encontrada"));

        // Calcula las emisiones
        Float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getKilometrajeTotal();

        // Crea entidad
        SubCategoriaCoche coche = new SubCategoriaCoche();
        coche.setTipoGasolina(dto.getTipoGasolina());
        coche.setNumeroCoches(dto.getNumeroCoches());
        coche.setMarca(dto.getMarca());
        coche.setKilometrajeTotal(dto.getKilometrajeTotal());
        coche.setEmisionesKgCO2_C(emisiones);
        coche.setEnviadoResultadoC(false);
        coche.setFechaRegistro(LocalDateTime.now());
        coche.setUsuario(usuario);
        coche.setCategoria(categoria);
        coche.setFactor(factor);

        ScocheRepository.save(coche);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(coche, SCocheDTO.class);
    }

    public SCocheDTO actualizar(Long id, SCocheCreateDTO dto, String username) {
        SubCategoriaCoche coche = ScocheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coche no encontrado"));

        // Validar que el usuario autenticado sea el dueño del registro
        if (!coche.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este coche");
        }

        // Solo actualiza los campos que el usuario quiera
        if (dto.getTipoGasolina() != null && !dto.getTipoGasolina().isBlank())
            coche.setTipoGasolina(dto.getTipoGasolina());
        if (dto.getMarca() != null && !dto.getMarca().isBlank())
            coche.setMarca(dto.getMarca());
        if (dto.getNumeroCoches() != null && dto.getNumeroCoches() > 0)
            coche.setNumeroCoches(dto.getNumeroCoches());
        if (dto.getKilometrajeTotal() != null && !Float.isNaN(dto.getKilometrajeTotal()))
            coche.setKilometrajeTotal(dto.getKilometrajeTotal());

        // Actualiza el factor automáticamente según el nombre
        String nombre = coche.getTipoGasolina().toLowerCase();
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

        coche.setFactor(factor);
        coche.setEmisionesKgCO2_C(factor.getFactorKgCO2PorUnidad() * coche.getKilometrajeTotal());
        coche.setFechaRegistro(LocalDateTime.now());

        ScocheRepository.save(coche);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(coche, SCocheDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuarioC() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ScocheRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando coche con ID: {}", id);
        ScocheRepository.deleteById(id);
        return "Registro eliminado";
    }
}