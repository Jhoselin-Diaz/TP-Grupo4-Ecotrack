package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.*;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.CategoriaRepository;
import com.example.tpgrupo4ecotrack.Repository.FactorEmisionRepository;
import com.example.tpgrupo4ecotrack.Repository.SServicioViviendaRepository;
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

    public List<SServicioViviendaDTO> listaServicioVAdmin() {
        log.info("Obteniendo lista de Servicio Vivienda");
        List<SubCategoriaServicioVivienda> servicios = SservicioViviendaRepository.findAll();
        List<SServicioViviendaDTO> sServicioDTO = new ArrayList<>();

        // Recorre cada objeto coche obtenido del repositorio
        for (SubCategoriaServicioVivienda servicio : servicios) {
            SServicioViviendaDTO dto = new SServicioViviendaDTO();
            dto.setIdServicios(servicio.getIdServicios());
            dto.setElectricidadKWh(servicio.getElectricidadKWh());
            dto.setCarbonKl(servicio.getCarbonKl());
            dto.setGasNaturalM3(servicio.getGasNaturalM3());
            dto.setEmisionesKgCO2_S(servicio.getEmisionesKgCO2_S());
            dto.setEnviadoResultadoS(servicio.getEnviadoResultadoS());
            dto.setFechaRegistro(servicio.getFechaRegistro());
            dto.setUsuarioid(servicio.getUsuario().getIdUsuario());
            dto.setCategoriaid(servicio.getCategoria().getIdCategoria());
            dto.setFactorid(servicio.getFactor().getIdFactor());
            sServicioDTO.add(dto);
        }
        return sServicioDTO;
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

    public List<SServicioViviendaDTO> Registrar(SServicioCreateDTO dto, String username) {

        System.out.println("===> INICIO REGISTRO SERVICIO VIVIENDA <===");
        System.out.println("Usuario autenticado: " + username);
        System.out.println("Datos recibidos: " + dto);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase("Servicio y Vivienda")
                .orElseThrow(() -> new RuntimeException("Categoría 'Servicio y Vivienda' no encontrada"));

        List<SServicioViviendaDTO> registros = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        // Electricidad
        if (dto.getElectricidadKWh() != null && dto.getElectricidadKWh() > 0) {
            System.out.println("→ Registrando electricidad...");
            FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase("ELEC_PERU")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: ELEC_PERU"));

            float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getElectricidadKWh();

            SubCategoriaServicioVivienda servicio = new SubCategoriaServicioVivienda();
            servicio.setCarbonKl(0f);
            servicio.setGasNaturalM3(0f);
            servicio.setElectricidadKWh(dto.getElectricidadKWh());
            servicio.setEmisionesKgCO2_S(emisiones);
            servicio.setFechaRegistro(LocalDateTime.now());
            servicio.setEnviadoResultadoS(true);
            servicio.setUsuario(usuario);
            servicio.setCategoria(categoria);
            servicio.setFactor(factor);

            SservicioViviendaRepository.save(servicio);
            registros.add(modelMapper.map(servicio, SServicioViviendaDTO.class));
        }

        // Gas Natural
        if (dto.getGasNaturalM3() != null && dto.getGasNaturalM3() > 0) {
            System.out.println("→ Registrando gas natural...");
            FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase("GAS_NAT")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: GAS_NAT"));

            float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getGasNaturalM3();

            SubCategoriaServicioVivienda servicio = new SubCategoriaServicioVivienda();
            servicio.setCarbonKl(0f);
            servicio.setGasNaturalM3(dto.getGasNaturalM3());
            servicio.setElectricidadKWh(0f);
            servicio.setEmisionesKgCO2_S(emisiones);
            servicio.setFechaRegistro(LocalDateTime.now());
            servicio.setEnviadoResultadoS(true);
            servicio.setUsuario(usuario);
            servicio.setCategoria(categoria);
            servicio.setFactor(factor);

            SservicioViviendaRepository.save(servicio);
            registros.add(modelMapper.map(servicio, SServicioViviendaDTO.class));
        }

        // Carbón
        if (dto.getCarbonKl() != null && dto.getCarbonKl() > 0) {
            System.out.println("→ Registrando carbón...");
            FactorEmision factor = factorEmisionRepository.findByCodigoIgnoreCase("CARBON")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: CARBON"));

            float emisiones = factor.getFactorKgCO2PorUnidad() * dto.getCarbonKl();

            SubCategoriaServicioVivienda servicio = new SubCategoriaServicioVivienda();
            servicio.setCarbonKl(dto.getCarbonKl());
            servicio.setGasNaturalM3(0f);
            servicio.setElectricidadKWh(0f);
            servicio.setEmisionesKgCO2_S(emisiones);
            servicio.setFechaRegistro(LocalDateTime.now());
            servicio.setEnviadoResultadoS(true);
            servicio.setUsuario(usuario);
            servicio.setCategoria(categoria);
            servicio.setFactor(factor);

            SservicioViviendaRepository.save(servicio);
            registros.add(modelMapper.map(servicio, SServicioViviendaDTO.class));
        }

        if (registros.isEmpty()) {
            System.out.println("No se ingresó ningún valor válido");
            throw new RuntimeException("Debe ingresar al menos un valor mayor a 0");
        }

        System.out.println("Registro(s) completado(s): " + registros.size());
        return registros;
    }

    public SServicioViviendaDTO actualizar(Long id, SServicioCreateDTO dto, String username) {
        // Busca el registro
        SubCategoriaServicioVivienda servicio = SservicioViviendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de servicio no encontrado"));

        // Verifica que pertenece al usuario autenticado
        if (!servicio.getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para actualizar este registro");
        }

        // Actualiza solo los campos que el usuario envíe
        if (dto.getCarbonKl() != null && dto.getCarbonKl() >= 0)
            servicio.setCarbonKl(dto.getCarbonKl());
        if (dto.getGasNaturalM3() != null && dto.getGasNaturalM3() >= 0)
            servicio.setGasNaturalM3(dto.getGasNaturalM3());
        if (dto.getElectricidadKWh() != null && dto.getElectricidadKWh() >= 0)
            servicio.setElectricidadKWh(dto.getElectricidadKWh());

        // Recalcula las emisiones según los valores actuales
        float emisionesTotales = 0f;

        // Electricidad
        if (servicio.getElectricidadKWh() != null && servicio.getElectricidadKWh() > 0) {
            FactorEmision factorElectricidad = factorEmisionRepository.findByCodigoIgnoreCase("ELEC_PERU")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: ELECT_PERU"));
            emisionesTotales += factorElectricidad.getFactorKgCO2PorUnidad() * servicio.getElectricidadKWh();
            servicio.setFactor(factorElectricidad);
        }

        // Gas Natural
        if (servicio.getGasNaturalM3() != null && servicio.getGasNaturalM3() > 0) {
            FactorEmision factorGas = factorEmisionRepository.findByCodigoIgnoreCase("GAS_NAT")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: GAS_NAT"));
            emisionesTotales += factorGas.getFactorKgCO2PorUnidad() * servicio.getGasNaturalM3();
            servicio.setFactor(factorGas);
        }

        // Carbón
        if (servicio.getCarbonKl() != null && servicio.getCarbonKl() > 0) {
            FactorEmision factorCarbon = factorEmisionRepository.findByCodigoIgnoreCase("CARBON")
                    .orElseThrow(() -> new RuntimeException("Factor no encontrado: CARBON"));
            emisionesTotales += factorCarbon.getFactorKgCO2PorUnidad() * servicio.getCarbonKl();
            servicio.setFactor(factorCarbon);
        }

        // Actualiza datos comunes
        servicio.setEmisionesKgCO2_S(emisionesTotales);
        servicio.setFechaRegistro(LocalDateTime.now());
        servicio.setEnviadoResultadoS(true);

        // Guarda cambios
        SservicioViviendaRepository.save(servicio);

        // Mapea en el DTO
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(servicio, SServicioViviendaDTO.class);
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
