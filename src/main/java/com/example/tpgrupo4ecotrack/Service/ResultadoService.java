package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.ResultadoDTO;
import com.example.tpgrupo4ecotrack.Entity.*;
import com.example.tpgrupo4ecotrack.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SAlimentoRepository alimentoRepo;
    private final SRopaRepository ropaRepo;
    private final SCocheRepository cocheRepo;
    private final SAutobusRepository autobusRepo;
    private final SElectrodomesticoRepository electroRepo;
    private final SServicioViviendaRepository servicioRepo;
    private final ResultadoDetalleService resultadoDetalleService;
    private final ResultadoEquivalenciaService resultadoEquivalenciaService;

    public ResultadoService(ResultadoRepository resultadoRepository, CategoriaRepository categoriaRepository, SAlimentoRepository alimentoRepo,
                            SRopaRepository ropaRepo, SCocheRepository cocheRepo, SAutobusRepository autobusRepo,
                            SElectrodomesticoRepository electroRepo, SServicioViviendaRepository servicioRepo, ResultadoDetalleService resultadoDetalleService, ResultadoEquivalenciaService resultadoEquivalenciaService) {

        this.resultadoRepository = resultadoRepository;
        this.categoriaRepository = categoriaRepository;
        this.alimentoRepo = alimentoRepo;
        this.ropaRepo = ropaRepo;
        this.cocheRepo = cocheRepo;
        this.autobusRepo = autobusRepo;
        this.electroRepo = electroRepo;
        this.servicioRepo = servicioRepo;
        this.resultadoDetalleService = resultadoDetalleService;
        this.resultadoEquivalenciaService = resultadoEquivalenciaService;
    }

    // 🔹 Generar resultados agrupados por categoría
    public void generarResultadosPorHuella(HuellaCarbono huella) {
        Long usuarioId = huella.getUsuario().getIdUsuario();

        crearResultadoPorCategoria("Alimento", huella, alimentoRepo.findByUsuario_IdUsuario(usuarioId));
        crearResultadoPorCategoria("Ropa", huella, ropaRepo.findByUsuario_IdUsuario(usuarioId));
        crearResultadoPorCategoria("Coche", huella, cocheRepo.findByUsuario_IdUsuario(usuarioId));
        crearResultadoPorCategoria("Autobus", huella, autobusRepo.findByUsuario_IdUsuario(usuarioId));
        crearResultadoPorCategoria("Electrodomestico", huella, electroRepo.findByUsuario_IdUsuario(usuarioId));
        crearResultadoPorCategoria("Servicio y Vivienda", huella, servicioRepo.findByUsuario_IdUsuario(usuarioId));
    }

    // 🔹 Crear resultado por categoría
    private <T> void crearResultadoPorCategoria(String nombreCategoria, HuellaCarbono huella, List<T> registros) {
        if (registros == null || registros.isEmpty()) return;

        Categoria categoria = categoriaRepository.findByNombreCategoriaIgnoreCase(nombreCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría '" + nombreCategoria + "' no encontrada"));

        Resultado resultado = new Resultado();
        resultado.setCategoria(categoria);
        resultado.setHuella(huella);

        resultado = resultadoRepository.save(resultado);

        float totalEmisiones = resultadoDetalleService.crearDetallesPorCategoria(resultado, registros);

        // Luego generar equivalencias basadas en totalEmisiones
        resultadoEquivalenciaService.generarEquivalencias(resultado, totalEmisiones);
    }

    // Obtener resultados de un usuario
    public List<Resultado> obtenerResultadosPorUsuario(Long usuarioId) {
        return resultadoRepository.findByHuella_Usuario_IdUsuario(usuarioId);
    }




    public String eliminar(Long id) {
        log.warn("Eliminando resultado con ID: {}", id);
        resultadoRepository.deleteById(id);
        return "Registro eliminado";
    }
}
