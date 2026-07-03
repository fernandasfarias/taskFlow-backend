package com.taskflow.api.service;

import com.taskflow.api.dto.KanbanAtividadeDTO;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.enums.Status;
import com.taskflow.api.repository.AtividadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KanbanService {

    private final AtividadeRepository atividadeRepository;

    public KanbanService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    public List<KanbanAtividadeDTO> listarPorProjeto(UUID idProjeto) {
        return atividadeRepository.findByProjetoIdProjeto(idProjeto)
                .stream()
                .map(a -> new KanbanAtividadeDTO(
                        a.getIdAtividade(),
                        a.getNomeAtividade(),
                        a.getDescricaoAtividade(),
                        a.getDataInicio(),
                        a.getDataEntrega(),
                        a.getStatusAtividade().name(),
                        a.getColaboradores()
                                .stream()
                                .map(Colaborador::getNome)
                                .toList()
                ))
                .toList();
    }

    public void alterarStatus(UUID idAtividade, Status novoStatus) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setStatusAtividade(novoStatus);
        atividadeRepository.save(atividade);
    }
}