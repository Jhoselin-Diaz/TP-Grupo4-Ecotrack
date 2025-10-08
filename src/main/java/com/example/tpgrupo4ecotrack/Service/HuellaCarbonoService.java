package com.example.tpgrupo4ecotrack.Service;

import com.example.tpgrupo4ecotrack.DTO.HuellaDTO;
import com.example.tpgrupo4ecotrack.DTO.SAlimentoDTO;
import com.example.tpgrupo4ecotrack.Entity.HuellaCarbono;
import com.example.tpgrupo4ecotrack.Entity.ResultadoDetalle;
import com.example.tpgrupo4ecotrack.Entity.SubCategoriaAlimento;
import com.example.tpgrupo4ecotrack.Entity.Usuario;
import com.example.tpgrupo4ecotrack.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HuellaCarbonoService {

    private final HuellaCarbonoRepository huellaCarbonoRepository;
    private final ResultadoDetalleRepository resultadoDetalleRepository;
    private final SServicioViviendaRepository servicioRepo;
    private final SCocheRepository cocheRepo;
    private final SAlimentoRepository alimentoRepo;
    private final SElectrodomesticoRepository electroRepo;
    private final SAutobusRepository autobusRepo;
    private final SRopaRepository ropaRepo;
    private final UsuarioRepository usuarioRepository;

    public HuellaCarbonoService(HuellaCarbonoRepository huellaCarbonoRepository, ResultadoDetalleRepository resultadoDetalleRepository, SServicioViviendaRepository servicioRepo, SCocheRepository cocheRepo,
                                SAlimentoRepository alimentoRepo, SElectrodomesticoRepository electroRepo,
                                SAutobusRepository autobusRepo, SRopaRepository ropaRepo,  UsuarioRepository usuarioRepository) {
        this.huellaCarbonoRepository = huellaCarbonoRepository;
        this.resultadoDetalleRepository = resultadoDetalleRepository;
        this.servicioRepo = servicioRepo;
        this.cocheRepo = cocheRepo;
        this.alimentoRepo = alimentoRepo;
        this.electroRepo = electroRepo;
        this.autobusRepo = autobusRepo;
        this.ropaRepo = ropaRepo;
        this.usuarioRepository = usuarioRepository;
    }

    public List<HuellaDTO> lista() {
        log.info("Obteniendo lista de Huella Carbono");
        List<HuellaCarbono> huellas = huellaCarbonoRepository.findAll();
        List<HuellaDTO> huellaDTOS = new ArrayList<>();
        for (HuellaCarbono huella : huellas) {
            HuellaDTO dto = new HuellaDTO();
            dto.setIdHuella(huella.getIdHuella());
            dto.setPeriodo(huella.getPeriodo());
            dto.setTotalKgCO2(huella.getTotalKgCO2());
            dto.setFechaCalculo(huella.getFechaCalculo());
            huellaDTOS.add(dto);

        }
        return huellaDTOS;
    }

    public HuellaDTO calcularYGuardarHuellaPorId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Float total = 0f;

        Float servicio = servicioRepo.sumEmisionesByUsuario(usuarioId);
        Float coche = cocheRepo.sumEmisionesByUsuario(usuarioId);
        Float alimento = alimentoRepo.sumEmisionesByUsuario(usuarioId);
        Float electro = electroRepo.sumEmisionesByUsuario(usuarioId);
        Float autobus = autobusRepo.sumEmisionesByUsuario(usuarioId);
        Float ropa = ropaRepo.sumEmisionesByUsuario(usuarioId);

        total += (servicio != null ? servicio : 0f);
        total += (coche != null ? coche : 0f);
        total += (alimento != null ? alimento : 0f);
        total += (electro != null ? electro : 0f);
        total += (autobus != null ? autobus : 0f);
        total += (ropa != null ? ropa : 0f);

        HuellaCarbono huella = new HuellaCarbono();
        huella.setUsuario(usuario);
        huella.setTotalKgCO2(total.longValue());
        huella.setFechaCalculo(LocalDateTime.now());
        huella.setPeriodo(LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear());

        huellaCarbonoRepository.save(huella);

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(huella, HuellaDTO.class);
    }


    public String eliminar(Long id) {
        log.warn("Eliminando huella de carbono con ID: {}", id);
        huellaCarbonoRepository.deleteById(id);
        return "Registro eliminado";
    }
}