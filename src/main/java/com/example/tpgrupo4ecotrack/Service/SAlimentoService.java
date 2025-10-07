package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.Categoria;
import com.example.tpgrupo4ecotrack.Entity.FactorEmision;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.SAlimentoRepository;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
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
public class SAlimentoService {

    private final SAlimentoRepository SalimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FactorEmisionRepository factorEmisionRepository;
    private final CategoriaRepository categoriaRepository;


    public SAlimentoService(SAlimentoRepository SalimentoRepository, UsuarioRepository usuarioRepository, FactorEmisionRepository factorEmisionRepository, CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.SalimentoRepository = SalimentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.factorEmisionRepository = factorEmisionRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SAlimentoDTO> listaAlimentosAdmin() {
        log.info("Obteniendo lista de Alimentos");
        List<SubCategoriaAlimento> alimentos = SalimentoRepository.findAll();
        List<SAlimentoDTO> sAlimentoDTO = new ArrayList<>();
        for (SubCategoriaAlimento alimento : alimentos) {
            SAlimentoDTO dto = new SAlimentoDTO();
            dto.setIdAlimento(alimento.getIdAlimento());
            dto.setNombreAlimento(alimento.getNombreAlimento());
            dto.setEmisionesKgCO2_AL(alimento.getEmisionesKgCO2_AL());
            dto.setCantidadKg(alimento.getCantidadKg());
            dto.setEnviadoResultadoAL(alimento.getEnviadoResultadoAL());
            dto.setFechaRegistro(alimento.getFechaRegistro());
            dto.setUsuarioid(alimento.getUsuario().getIdUsuario());
            dto.setCategoriaid(alimento.getCategoria().getIdCategoria());
            dto.setFactorid(alimento.getFactor().getIdFactor());
            sAlimentoDTO.add(dto);

        }
        return sAlimentoDTO;
    }

    public List<ListaAlimentoPorUsuarioDTO> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<SubCategoriaAlimento> lista = SalimentoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        ModelMapper modelMapper = new ModelMapper();

        return lista.stream()
                .map(alimento -> modelMapper.map(alimento, ListaAlimentoPorUsuarioDTO.class))
                .toList();
    }

    public SAlimentoDTO Registrar(SAlimentoCreateDTO dto, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Detecta automáticamente el código del factor según el nombre
        String nombre = dto.getNombreAlimento().toLowerCase();
        String codigoFactor;

        if (nombre.contains("pollo")) codigoFactor = "POLLO";
        else if (nombre.contains("carne") || nombre.contains("res")) codigoFactor = "CARNE_RES";
        else if (nombre.contains("cerdo") || nombre.contains("chuleta")) codigoFactor = "CERDO";
        else if (nombre.contains("leche") || nombre.contains("mantequilla") || nombre.contains("griego")
                || nombre.contains("yogurt") || nombre.contains("evaporada")) codigoFactor = "LACTEOS";
        else if (nombre.contains("limonada") || nombre.contains("gaseosas")
                || nombre.contains("jugo") || nombre.contains("refresco") || nombre.contains("chicha")
                || nombre.contains("gaseosa")) codigoFactor = "BEBIDAS";
        else if (nombre.contains("brocoli") || nombre.contains("apio") || nombre.contains("tomate")
                || nombre.contains("pepino") || nombre.contains("zapallo") || nombre.contains("papa")
                || nombre.contains("cebolla")) codigoFactor = "VEGETALES";
        else if (nombre.contains("manzana") || nombre.contains("maracuya") || nombre.contains("pera")
                || nombre.contains("uva") || nombre.contains("melon") || nombre.contains("sandia")
                || nombre.contains("platano")) codigoFactor = "FRUTAS";
        else codigoFactor = "";

        // Busca el factor según el código
        FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        // Busca la categoría
        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Alimentos")
                .orElseThrow(() -> new RuntimeException("Categoría 'Alimentos' no encontrada"));

        // Calcula las emisiones
        Float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getCantidadKg();

        // Crea entidad
        SubCategoriaAlimento alimento = new SubCategoriaAlimento();
        alimento.setNombreAlimento(dto.getNombreAlimento());
        alimento.setCantidadKg(dto.getCantidadKg());
        alimento.setEmisionesKgCO2_AL(emisiones);
        alimento.setEnviadoResultadoAL(false);
        alimento.setFechaRegistro(LocalDateTime.now());
        alimento.setUsuario(usuario);
        alimento.setCategoria(categoria);
        alimento.setFactor(factor);

        SalimentoRepository.save(alimento);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(alimento, SAlimentoDTO.class);
    }

    public SAlimentoDTO actualizar(Long id, SAlimentoDTO SAlimentoDTO) {
        SubCategoriaAlimento alimento = SalimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factor Emisión no encontrado"));

        if (SAlimentoDTO.getNombreAlimento() != null && !SAlimentoDTO.getNombreAlimento().isBlank())
            alimento.setNombreAlimento(SAlimentoDTO.getNombreAlimento());
        if (SAlimentoDTO.getEmisionesKgCO2_AL() != null &&
                !Float.isNaN(SAlimentoDTO.getEmisionesKgCO2_AL())) {
            alimento.setEmisionesKgCO2_AL(SAlimentoDTO.getEmisionesKgCO2_AL());
        }
        if (SAlimentoDTO.getCantidadKg() != null &&
                !Float.isNaN(SAlimentoDTO.getCantidadKg())) {
            alimento.setCantidadKg(SAlimentoDTO.getCantidadKg());
        }

        alimento.setFechaRegistro(LocalDateTime.now());


        SalimentoRepository.save(alimento);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(alimento, SAlimentoDTO.class);
    }


    public SAlimentoDTO actualizarPorUser(Long id, SAlimentoCreateDTO dto, String username) {
        SubCategoriaAlimento alimento = SalimentoRepository.findById(id)
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

        // Actualiza el factor automáticamente según el nombre
        String nombre = alimento.getNombreAlimento().toLowerCase();
        String codigoFactor;

        if (nombre.contains("pollo")) codigoFactor = "POLLO";
        else if (nombre.contains("carne") || nombre.contains("res")) codigoFactor = "CARNE_RES";
        else if (nombre.contains("cerdo") || nombre.contains("chuleta")) codigoFactor = "CERDO";
        else if (nombre.contains("leche") || nombre.contains("mantequilla") || nombre.contains("griego")
                || nombre.contains("yogurt") || nombre.contains("evaporada")) codigoFactor = "LACTEOS";
        else if (nombre.contains("limonada") || nombre.contains("gaseosas")
                || nombre.contains("jugo") || nombre.contains("refresco") || nombre.contains("chicha")
                || nombre.contains("gaseosa")) codigoFactor = "BEBIDAS";
        else if (nombre.contains("brocoli") || nombre.contains("apio") || nombre.contains("tomate")
                || nombre.contains("pepino") || nombre.contains("zapallo") || nombre.contains("papa")
                || nombre.contains("cebolla")) codigoFactor = "VEGETALES";
        else if (nombre.contains("manzana") || nombre.contains("maracuya") || nombre.contains("pera")
                || nombre.contains("uva") || nombre.contains("melon") || nombre.contains("sandia")
                || nombre.contains("platano")) codigoFactor = "FRUTAS";
        else codigoFactor = "";

        FactorEmision factor = (FactorEmision) factorEmisionRepository.findByCodigoIgnoreCase(codigoFactor)
                .orElseThrow(() -> new RuntimeException("Factor no encontrado: " + codigoFactor));

        alimento.setFactor(factor);
        alimento.setEmisionesKgCO2_AL(factor.getFactorKgCO2PorUnidad() * alimento.getCantidadKg());
        alimento.setFechaRegistro(LocalDateTime.now());

        SalimentoRepository.save(alimento);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(alimento, SAlimentoDTO.class);
    }

    public Float calcularTotalEmisionesDelUsuarioA() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return SalimentoRepository.sumEmisionesByUsuario(usuario.getIdUsuario());
    }

    public String eliminar(Long id) {
        log.warn("Eliminando alimento con ID: {}", id);
        SalimentoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
