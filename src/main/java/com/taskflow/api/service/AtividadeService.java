package com.taskflow.api.service;

import com.taskflow.api.dto.*;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Milestone;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.ProjetoRepository;
import com.taskflow.api.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final ProjetoRepository projetoRepository;
    private final TarefaRepository tarefaRepository;

    public AtividadeResponseDTO criar(AtividadeRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.idProjeto())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Milestone milestone = null;

        Atividade atividade = Atividade.builder()
                .nomeAtividade(dto.nomeAtividade())
                .descricaoAtividade(dto.descricaoAtividade())
                .dataInicio(dto.dataInicio())
                .dataEntrega(dto.dataEntrega())
                .statusAtividade(dto.statusAtividade())
                .projeto(projeto)
                .milestone(milestone)
                .build();

        return toResponse(atividadeRepository.save(atividade));
    }

    public List<AtividadeResponseDTO> listarPorProjeto(UUID idProjeto) {
        return atividadeRepository.findByProjetoIdProjeto(idProjeto)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AtividadeResponseDTO buscarPorId(UUID idAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        return toResponse(atividade);
    }

    public void deletar(UUID idAtividade) {
        if (!atividadeRepository.existsById(idAtividade)) {
            throw new RuntimeException("Atividade não encontrada");
        }

        atividadeRepository.deleteById(idAtividade);
    }

    private AtividadeResponseDTO toResponse(Atividade atividade) {
        return new AtividadeResponseDTO(
                atividade.getIdAtividade(),
                atividade.getNomeAtividade(),
                atividade.getDescricaoAtividade(),
                atividade.getDataInicio(),
                atividade.getDataEntrega(),
                atividade.getStatusAtividade(),
                atividade.getProjeto().getIdProjeto(),
                atividade.getMilestone() != null
                        ? atividade.getMilestone().getIdMilestone()
                        : null
        );
    }

    public void associarColaborador(AssociarColaboradorAtividadeDTO dto) {
        atividadeRepository.associar(
                dto.idColaborador(),
                dto.idAtividade()
        );
    }

    public void atualizar(UUID id, AtualizarAtividadeDTO dto) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setNomeAtividade(dto.nomeAtividade());
        atividade.setDescricaoAtividade(dto.descricaoAtividade());
        atividade.setDataInicio(dto.dataInicio());
        atividade.setDataEntrega(dto.dataEntrega());
        atividade.setStatusAtividade(dto.statusAtividade());

        atividadeRepository.save(atividade);
    }

    public AtividadeDetalhesDTO buscarDetalhes(UUID idAtividade) {
        Atividade atividadeEntity = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        AtividadeResponseDTO atividade = new AtividadeResponseDTO(
                atividadeEntity.getIdAtividade(),
                atividadeEntity.getNomeAtividade(),
                atividadeEntity.getDescricaoAtividade(),
                atividadeEntity.getDataInicio(),
                atividadeEntity.getDataEntrega(),
                atividadeEntity.getStatusAtividade(),
                atividadeEntity.getProjeto().getIdProjeto(),
                atividadeEntity.getMilestone() != null
                        ? atividadeEntity.getMilestone().getIdMilestone()
                        : null
        );

        List<TarefaDTO> tarefas = tarefaRepository
                .findByAtividade_IdAtividade(idAtividade)
                .stream()
                .map(tarefa -> new TarefaDTO(
                        tarefa.getIdTarefa(),
                        tarefa.getNomeTarefa(),
                        tarefa.getDataInicio(),
                        tarefa.getDataEntrega(),
                        tarefa.getStatusTarefa().name()
                ))
                .toList();

        MilestoneDTO milestone = atividadeEntity.getMilestone() != null ? new MilestoneDTO(
                atividadeEntity.getMilestone().getIdMilestone(),
                atividadeEntity.getMilestone().getNomeMilestone(),
                atividadeEntity.getMilestone().getDescricao(),
                atividadeEntity.getMilestone().getDataPrevista()) : null;

        return new AtividadeDetalhesDTO(
                atividade,
                tarefas,
                milestone
        );
    }
}