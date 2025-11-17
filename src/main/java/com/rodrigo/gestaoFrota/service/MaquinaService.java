package com.rodrigo.gestaoFrota.service;

import com.rodrigo.gestaoFrota.entity.Maquina;
import com.rodrigo.gestaoFrota.repository.MaquinaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    public Maquina salvar(Maquina maquina) {
        return maquinaRepository.save(maquina);
    }

    public List<Maquina> listarTodas() {
        return maquinaRepository.findAll();
    }

    public Maquina buscarPorId(Long id) {
        return maquinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Máquina não encontrada"));
    }

    public Maquina atualizar(Long id, Maquina maquinaAtualizada) {
        Maquina existente = buscarPorId(id);
        existente.setNome(maquinaAtualizada.getNome());
        existente.setTipo(maquinaAtualizada.getTipo());
        existente.setModelo(maquinaAtualizada.getModelo());
        existente.setAno(maquinaAtualizada.getAno());
        existente.setNumeroSerie(maquinaAtualizada.getNumeroSerie());
        existente.setHorimetroAtual(maquinaAtualizada.getHorimetroAtual());
        existente.setHorimetroProxRevisao(maquinaAtualizada.getHorimetroProxRevisao());
        existente.setDataUltimaManutencao(maquinaAtualizada.getDataUltimaManutencao());
        existente.setDataProxManutencao(maquinaAtualizada.getDataProxManutencao());
        existente.setAtivo(maquinaAtualizada.getAtivo());
        return maquinaRepository.save(existente);
    }

    public void excluir(Long id) {
        maquinaRepository.deleteById(id);
    }

    // Alertas baseados em horas
    public List<Maquina> maquinasProximasDaRevisaoPorHoras(double margemHoras) {
        // Ex: pegar máquinas onde horimetroAtual >= horimetroProxRevisao - margem
        // Como o método do repository é simples, a lógica fica aqui
        List<Maquina> todas = maquinaRepository.findAll();
        return todas.stream()
                .filter(m -> m.getHorimetroProxRevisao() != null
                        && m.getHorimetroAtual() != null
                        && m.getHorimetroAtual() >= (m.getHorimetroProxRevisao() - margemHoras))
                .toList();
    }

    // Alertas baseados em data
    public List<Maquina> maquinasProximasDaRevisaoPorData(int diasAntes) {
        LocalDate limite = LocalDate.now().plusDays(diasAntes);
        return maquinaRepository.findByDataProxManutencaoBefore(limite);
    }
}

