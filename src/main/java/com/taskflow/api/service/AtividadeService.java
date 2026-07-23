package com.taskflow.api.service;

import com.taskflow.api.dto.*;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Milestone;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.enums.Status;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.MilestoneRepository;
import com.taskflow.api.repository.ProjetoRepository;
import com.taskflow.api.repository.TarefaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final ProjetoRepository projetoRepository;
    private final TarefaRepository tarefaRepository;
    private final MilestoneRepository milestoneRepository;

    @SuppressWarnings("null")
    public AtividadeResponseDTO criar(AtividadeRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.idProjeto())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        Atividade atividade = Atividade.builder()
                .nomeAtividade(dto.nomeAtividade())
                .descricaoAtividade(dto.descricaoAtividade())
                .dataInicio(dto.dataInicio())
                .dataEntrega(dto.dataEntrega())
                .statusAtividade(dto.statusAtividade())
                .projeto(projeto)
                .milestone(null)
                .build();

        return toResponse(atividadeRepository.save(atividade));
    }

    public List<AtividadeResponseDTO> listarPorProjeto(@NonNull UUID idProjeto) {
        return atividadeRepository.findByProjetoIdProjeto(idProjeto)
                .stream()
                .filter(a -> a != null)
                .map(this::toResponse)
                .toList();
    }

    public AtividadeResponseDTO buscarPorId(@NonNull UUID idAtividade) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        return toResponse(atividade);
    }

    @Transactional
    public void deletar(@NonNull UUID idAtividade) {
        if (!atividadeRepository.existsById(idAtividade)) {
        throw new RuntimeException("Atividade não encontrada");
    }

    // Remove colaboradores associados às tarefas
    tarefaRepository.deleteColaboradorTarefasByAtividade(idAtividade);

    // Remove tarefas
    tarefaRepository.deleteByAtividade(idAtividade);

    // Remove a atividade
    atividadeRepository.deleteById(idAtividade);
}

    private AtividadeResponseDTO toResponse(Atividade atividade) {
        Projeto proj = atividade.getProjeto();
        UUID idProj = (proj != null) ? proj.getIdProjeto() : null;

        Milestone m = atividade.getMilestone();
        UUID idMile = (m != null) ? m.getIdMilestone() : null;

        return new AtividadeResponseDTO(
                atividade.getIdAtividade(),
                atividade.getNomeAtividade(),
                atividade.getDescricaoAtividade(),
                atividade.getDataInicio(),
                atividade.getDataEntrega(),
                atividade.getStatusAtividade(),
                idProj,
                idMile);
    }

    public void associarColaborador(AssociarColaboradorAtividadeDTO dto) {
        atividadeRepository.associar(dto.idColaborador(), dto.idAtividade());
    }

    public void atualizar(@NonNull UUID id, AtualizarAtividadeDTO dto) {
        Atividade atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setNomeAtividade(dto.nomeAtividade());
        atividade.setDescricaoAtividade(dto.descricaoAtividade());
        atividade.setDataInicio(dto.dataInicio());
        atividade.setDataEntrega(dto.dataEntrega());
        atividade.setStatusAtividade(dto.statusAtividade());

        atividadeRepository.save(atividade);
    }

    public AtividadeDetalhesDTO buscarDetalhes(@NonNull UUID idAtividade) {
        Atividade atividadeEntity = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        AtividadeResponseDTO atividade = toResponse(atividadeEntity);

        List<TarefaDTO> tarefas = tarefaRepository
                .findByAtividade_IdAtividade(idAtividade)
                .stream()
                .filter(t -> t != null)
                .map(tarefa -> new TarefaDTO(
                        tarefa.getIdTarefa(),
                        tarefa.getAtividade().getIdAtividade(), // Correção do construtor
                        tarefa.getNomeTarefa(),
                        tarefa.getDataInicio(),
                        tarefa.getDataEntrega(),
                        tarefa.getStatusTarefa().name()))
                .toList();

        MilestoneDTO milestone = null;
        if (atividadeEntity.getMilestone() != null) {
            Milestone m = atividadeEntity.getMilestone();

            int total = (m.getEtapas() != null) ? m.getEtapas().size() : 0;
            int concluidas = (m.getEtapas() != null)
                    ? (int) m.getEtapas().stream()
                            .filter(e -> e != null && Boolean.TRUE.equals(e.getConcluida()))
                            .count()
                    : 0;
            int progresso = (total == 0) ? 0 : (concluidas * 100) / total;

            List<EtapaDTO> etapasDTO = (m.getEtapas() != null)
                    ? m.getEtapas().stream()
                            .filter(e -> e != null)
                            .map(e -> new EtapaDTO(e.getIdEtapa(), e.getNomeEtapa(), e.getConcluida()))
                            .collect(Collectors.toList())
                    : List.of();

            milestone = new MilestoneDTO(
                    m.getIdMilestone(),
                    m.getNomeMilestone(),
                    m.getDescricao(),
                    m.getDataPrevista(),
                    etapasDTO,
                    progresso);
        }

        return new AtividadeDetalhesDTO(atividade, tarefas, milestone);
    }

    public void alterarStatus(@NonNull UUID idAtividade, Status novoStatus) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        atividade.setStatusAtividade(novoStatus);
        atividadeRepository.save(atividade);
    }

    public List<KanbanAtividadeDTO> listarPorProjetoKanban(@NonNull UUID idProjeto) {
        return atividadeRepository.findByProjetoIdProjeto(idProjeto)
                .stream()
                .filter(a -> a != null)
                .map(a -> {
                    List<String> nomesColab = (a.getColaboradores() != null)
                            ? a.getColaboradores().stream()
                                    .filter(c -> c != null && c.getNome() != null)
                                    .map(c -> c.getNome())
                                    .toList()
                            : List.of();

                    return new KanbanAtividadeDTO(
                            a.getIdAtividade(),
                            a.getNomeAtividade(),
                            a.getDescricaoAtividade(),
                            a.getDataInicio(),
                            a.getDataEntrega(),
                            a.getStatusAtividade().name(),
                            nomesColab);
                })
                .toList();
    }
}