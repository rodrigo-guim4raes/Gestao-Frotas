package com.rodrigo.gestaoFrota.controller;

import com.rodrigo.gestaoFrota.entity.Maquina;
import com.rodrigo.gestaoFrota.service.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/maquinas")
@CrossOrigin(origins = "*")
public class MaquinaController {

    private final MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @PostMapping
    public ResponseEntity<Maquina> criar(@RequestBody Maquina maquina) {
        Maquina salva = maquinaService.salvar(maquina);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Maquina> listar() {
        return maquinaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Maquina buscarPorId(@PathVariable Long id) {
        return maquinaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Maquina atualizar(@PathVariable Long id, @RequestBody Maquina maquina) {
        return maquinaService.atualizar(id, maquina);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        maquinaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alertas/horas")
    public List<Maquina> alertasPorHoras(
            @RequestParam(defaultValue = "10") double margemHoras) {
        return maquinaService.maquinasProximasDaRevisaoPorHoras(margemHoras);
    }

    @GetMapping("/alertas/datas")
    public List<Maquina> alertasPorData(
            @RequestParam(defaultValue = "7") int diasAntes) {
        return maquinaService.maquinasProximasDaRevisaoPorData(diasAntes);
    }
}

