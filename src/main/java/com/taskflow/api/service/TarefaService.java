package com.taskflow.api.service;

import com.taskflow.api.dto.AssociarColaboradorTarefaDTO;
import com.taskflow.api.dto.CriarTarefaDTO;
import com.taskflow.api.dto.TarefaRequestDTO;
import com.taskflow.api.dto.TarefaResponseDTO;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.entity.Tarefa;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.ColaboradorRepository;
import com.taskflow.api.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    private final ColaboradorRepository colaboradorRepository;

    private final AtividadeRepository atividadeRepository;

    @Transactional
    public TarefaResponseDTO criar(UUID idAtividade, CriarTarefaDTO dto) {

        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Tarefa tarefa = new Tarefa();

        tarefa.setNomeTarefa(dto.nomeTarefa());
        tarefa.setDataInicio(dto.dataInicio());
        tarefa.setDataEntrega(dto.dataEntrega());
        tarefa.setStatusTarefa(dto.statusTarefa());

        tarefa.setAtividade(atividade);

        tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getNomeTarefa(),
                tarefa.getDataInicio(),
                tarefa.getDataEntrega(),
                tarefa.getStatusTarefa()
        );
    }

    @Transactional
    public void excluir(UUID idTarefa) {

        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefaRepository.delete(tarefa);
    }

    @Transactional
    public TarefaResponseDTO buscar(UUID idTarefa) {

        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getNomeTarefa(),
                tarefa.getDataInicio(),
                tarefa.getDataEntrega(),
                tarefa.getStatusTarefa()
        );
    }

    @Transactional
    public TarefaResponseDTO editar(UUID idTarefa, TarefaRequestDTO request) {

        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setNomeTarefa(request.nomeTarefa());
        tarefa.setDataInicio(request.dataInicio());
        tarefa.setDataEntrega(request.dataEntrega());
        tarefa.setStatusTarefa(request.statusTarefa());

        tarefaRepository.save(tarefa);

        return new TarefaResponseDTO(
                tarefa.getIdTarefa(),
                tarefa.getNomeTarefa(),
                tarefa.getDataInicio(),
                tarefa.getDataEntrega(),
                tarefa.getStatusTarefa()
        );
    }

    @Transactional
    public void associarColaborador(AssociarColaboradorTarefaDTO dto) {
       tarefaRepository.associar(
               dto.idColaborador(),
               dto.idTarefa()
       );
    }

}
