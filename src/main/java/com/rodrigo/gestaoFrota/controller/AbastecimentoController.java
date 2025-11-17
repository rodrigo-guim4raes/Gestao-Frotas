package com.rodrigo.gestaoFrota.controller;

import com.rodrigo.gestaoFrota.entity.Abastecimento;
import com.rodrigo.gestaoFrota.service.AbastecimentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/abastecimentos")
@CrossOrigin(origins = "*")
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    public AbastecimentoController(AbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @PostMapping("/maquina/{maquinaId}")
    public Abastecimento registrar(@PathVariable Long maquinaId,
                                   @RequestBody Abastecimento abastecimento) {
        return abastecimentoService.registrarAbastecimento(maquinaId, abastecimento);
    }

    @GetMapping("/maquina/{maquinaId}")
    public List<Abastecimento> listarPorMaquina(
            @PathVariable Long maquinaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return abastecimentoService.listarPorMaquina(maquinaId, dataInicio, dataFim);
    }

    @GetMapping("/maquina/{maquinaId}/totais")
    public TotaisAbastecimentoResponse totais(
            @PathVariable Long maquinaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        Double litros = abastecimentoService.totalLitros(maquinaId, dataInicio, dataFim);
        Double custo = abastecimentoService.totalCusto(maquinaId, dataInicio, dataFim);
        return new TotaisAbastecimentoResponse(litros, custo);
    }

    @GetMapping("/maquina/{maquinaId}/consumo-medio")
    public Double consumoMedio(
            @PathVariable Long maquinaId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return abastecimentoService.calcularConsumoMedioLitrosPorHora(maquinaId, dataInicio, dataFim);
    }

    public record TotaisAbastecimentoResponse(Double litros, Double custoTotal) {}
}

