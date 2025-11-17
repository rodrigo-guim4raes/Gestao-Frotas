package com.rodrigo.gestaoFrota.service;

import com.rodrigo.gestaoFrota.entity.Abastecimento;
import com.rodrigo.gestaoFrota.entity.Maquina;
import com.rodrigo.gestaoFrota.repository.AbastecimentoRepository;
import com.rodrigo.gestaoFrota.repository.MaquinaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final MaquinaRepository maquinaRepository;

    public AbastecimentoService(AbastecimentoRepository abastecimentoRepository,
                                MaquinaRepository maquinaRepository) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.maquinaRepository = maquinaRepository;
    }

    public Abastecimento registrarAbastecimento(Long maquinaId, Abastecimento abastecimento) {
        Maquina maquina = maquinaRepository.findById(maquinaId)
                .orElseThrow(() -> new RuntimeException("Máquina não encontrada"));
        abastecimento.setMaquina(maquina);
        if (abastecimento.getData() == null) {
            abastecimento.setData(LocalDate.now());
        }
        return abastecimentoRepository.save(abastecimento);
    }

    public List<Abastecimento> listarPorMaquina(Long maquinaId,
                                                LocalDate dataInicio,
                                                LocalDate dataFim) {
        if (dataInicio != null && dataFim != null) {
            return abastecimentoRepository
                    .findByMaquinaIdAndDataBetweenOrderByDataAsc(maquinaId, dataInicio, dataFim);
        }
        return abastecimentoRepository.findByMaquinaIdOrderByDataAsc(maquinaId);
    }

    public Double totalLitros(Long maquinaId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            List<Abastecimento> lista = abastecimentoRepository
                    .findByMaquinaIdOrderByDataAsc(maquinaId);
            return lista.stream()
                    .mapToDouble(a -> a.getLitros() != null ? a.getLitros() : 0.0)
                    .sum();
        }
        return abastecimentoRepository.totalLitrosNoPeriodo(maquinaId, dataInicio, dataFim);
    }

    public Double totalCusto(Long maquinaId, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            List<Abastecimento> lista = abastecimentoRepository
                    .findByMaquinaIdOrderByDataAsc(maquinaId);
            return lista.stream()
                    .mapToDouble(a -> a.getCustoTotal() != null ? a.getCustoTotal() : 0.0)
                    .sum();
        }
        return abastecimentoRepository.totalCustoNoPeriodo(maquinaId, dataInicio, dataFim);
    }

    /**
     * Consumo médio em litros/hora no período informado.
     *
     * Fórmula: totalLitros / (horimetroFinal - horimetroInicial)
     */
    public Double calcularConsumoMedioLitrosPorHora(Long maquinaId,
                                                    LocalDate dataInicio,
                                                    LocalDate dataFim) {
        List<Abastecimento> abastecimentos;

        if (dataInicio != null && dataFim != null) {
            abastecimentos = abastecimentoRepository
                    .findByMaquinaIdAndDataBetweenOrderByDataAsc(maquinaId, dataInicio, dataFim);
        } else {
            abastecimentos = abastecimentoRepository
                    .findByMaquinaIdOrderByDataAsc(maquinaId);
        }

        if (abastecimentos.size() < 2) {
            // Não há dados suficientes para calcular consumo médio
            return null;
        }

        // Garante ordenação por data (e horímetro se quiser refinar)
        abastecimentos.sort(Comparator.comparing(Abastecimento::getData));

        double litrosTotal = abastecimentos.stream()
                .mapToDouble(a -> a.getLitros() != null ? a.getLitros() : 0.0)
                .sum();

        Abastecimento primeiro = abastecimentos.get(0);
        Abastecimento ultimo = abastecimentos.get(abastecimentos.size() - 1);

        if (primeiro.getHorimetro() == null || ultimo.getHorimetro() == null) {
            return null;
        }

        double horasTrabalhadas = ultimo.getHorimetro() - primeiro.getHorimetro();

        if (horasTrabalhadas <= 0) {
            return null;
        }

        return litrosTotal / horasTrabalhadas; // litros por hora
    }
}

