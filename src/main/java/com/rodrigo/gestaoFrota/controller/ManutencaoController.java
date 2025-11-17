package com.rodrigo.gestaoFrota.controller;

import com.rodrigo.gestaoFrota.entity.Manutencao;
import com.rodrigo.gestaoFrota.service.ManutencaoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
@CrossOrigin(origins = "*")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @PostMapping("/maquina/{maquinaId}")
    public Manutencao registrar(@PathVariable Long maquinaId,
                                @RequestBody Manutencao manutencao) {
        return manutencaoService.registrarManutencao(maquinaId, manutencao);
    }

    @GetMapping("/maquina/{maquinaId}")
    public List<Manutencao> listarPorMaquina(@PathVariable Long maquinaId) {
        return manutencaoService.listarPorMaquina(maquinaId);
    }

    @GetMapping("/maquina/{maquinaId}/total-gasto")
    public Double totalGasto(@PathVariable Long maquinaId) {
        return manutencaoService.totalGastoEmManutencaoPorMaquina(maquinaId);
    }
}

