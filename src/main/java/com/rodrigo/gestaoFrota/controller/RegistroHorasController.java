package com.rodrigo.gestaoFrota.controller;

import com.rodrigo.gestaoFrota.entity.RegistroHoras;
import com.rodrigo.gestaoFrota.service.RegistroHorasService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/horas")
@CrossOrigin(origins = "*")
public class RegistroHorasController {

    private final RegistroHorasService registroHorasService;

    public RegistroHorasController(RegistroHorasService registroHorasService) {
        this.registroHorasService = registroHorasService;
    }

    @PostMapping("/maquina/{maquinaId}")
    public RegistroHoras registrar(@PathVariable Long maquinaId,
                                   @RequestBody RegistroHoras registro) {
        return registroHorasService.registrarHoras(maquinaId, registro);
    }

    @GetMapping("/maquina/{maquinaId}")
    public List<RegistroHoras> listarPorMaquina(
            @PathVariable Long maquinaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return registroHorasService.listarPorMaquina(maquinaId, dataInicio, dataFim);
    }

    @GetMapping("/maquina/{maquinaId}/total")
    public Double totalHoras(
            @PathVariable Long maquinaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return registroHorasService.totalHorasTrabalhadas(maquinaId, dataInicio, dataFim);
    }
}

