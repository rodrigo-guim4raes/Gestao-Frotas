package com.rodrigo.gestaoFrota.service;

import com.rodrigo.gestaoFrota.entity.Manutencao;
import com.rodrigo.gestaoFrota.entity.Maquina;
import com.rodrigo.gestaoFrota.repository.ManutencaoRepository;
import com.rodrigo.gestaoFrota.repository.MaquinaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final MaquinaRepository maquinaRepository;

    public ManutencaoService(ManutencaoRepository manutencaoRepository,
                             MaquinaRepository maquinaRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.maquinaRepository = maquinaRepository;
    }

    public Manutencao registrarManutencao(Long maquinaId, Manutencao manutencao) {
        Maquina maquina = maquinaRepository.findById(maquinaId)
                .orElseThrow(() -> new RuntimeException("Máquina não encontrada"));
        manutencao.setMaquina(maquina);
        return manutencaoRepository.save(manutencao);
    }

    public List<Manutencao> listarPorMaquina(Long maquinaId) {
        // implementação simples: filtrar na memória; o ideal seria query no repo
        return manutencaoRepository.findAll().stream()
                .filter(m -> m.getMaquina().getId().equals(maquinaId))
                .toList();
    }

    public Double totalGastoEmManutencaoPorMaquina(Long maquinaId) {
        return manutencaoRepository.totalGastoPorMaquina(maquinaId);
    }
}

