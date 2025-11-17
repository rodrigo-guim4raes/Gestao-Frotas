package com.rodrigo.gestaoFrota.service;

import com.rodrigo.gestaoFrota.entity.Maquina;
import com.rodrigo.gestaoFrota.entity.RegistroHoras;
import com.rodrigo.gestaoFrota.repository.MaquinaRepository;
import com.rodrigo.gestaoFrota.repository.RegistroHorasRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroHorasService {

    private final RegistroHorasRepository registroHorasRepository;
    private final MaquinaRepository maquinaRepository;

    public RegistroHorasService(RegistroHorasRepository registroHorasRepository,
                                MaquinaRepository maquinaRepository) {
        this.registroHorasRepository = registroHorasRepository;
        this.maquinaRepository = maquinaRepository;
    }

    public RegistroHoras registrarHoras(Long maquinaId, RegistroHoras registro) {
        Maquina maquina = maquinaRepository.findById(maquinaId)
                .orElseThrow(() -> new RuntimeException("Máquina não encontrada"));

        registro.setMaquina(maquina);
        if (registro.getData() == null) {
            registro.setData(LocalDate.now());
        }
        return registroHorasRepository.save(registro);
    }

    public List<RegistroHoras> listarPorMaquina(Long maquinaId,
                                                LocalDate dataInicio,
                                                LocalDate dataFim) {
        if (dataInicio != null && dataFim != null) {
            return registroHorasRepository
                    .findByMaquinaIdAndDataBetweenOrderByDataAsc(maquinaId, dataInicio, dataFim);
        }
        return registroHorasRepository.findByMaquinaIdOrderByDataAsc(maquinaId);
    }

    public Double totalHorasTrabalhadas(Long maquinaId,
                                        LocalDate dataInicio,
                                        LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            // Se não informar período, considera tudo
            List<RegistroHoras> registros = registroHorasRepository
                    .findByMaquinaIdOrderByDataAsc(maquinaId);
            return registros.stream()
                    .mapToDouble(r -> r.getHorasTrabalhadas() != null ? r.getHorasTrabalhadas() : 0.0)
                    .sum();
        }
        return registroHorasRepository.totalHorasTrabalhadasNoPeriodo(maquinaId, dataInicio, dataFim);
    }
}

