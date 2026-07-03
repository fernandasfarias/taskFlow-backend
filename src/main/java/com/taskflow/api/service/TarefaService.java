package com.taskflow.api.service;

import com.taskflow.api.dto.CriarTarefaDTO;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Tarefa;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    private final AtividadeRepository atividadeRepository;

    @Transactional
    public void criar(UUID idAtividade, CriarTarefaDTO dto) {

        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Tarefa tarefa = new Tarefa();

        tarefa.setNomeTarefa(dto.nomeTarefa());
        tarefa.setDataInicio(dto.dataInicio());
        tarefa.setDataEntrega(dto.dataEntrega());
        tarefa.setStatusTarefa(dto.statusTarefa());

        tarefa.setAtividade(atividade);

        tarefaRepository.save(tarefa);
    }

    @Transactional
    public void excluir(UUID idTarefa) {

        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefaRepository.delete(tarefa);
    }

}
