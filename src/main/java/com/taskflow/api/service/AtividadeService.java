package com.taskflow.api.service;

import com.taskflow.api.dto.AssociarColaboradorAtividadeDTO;
import com.taskflow.api.dto.AtividadeRequestDTO;
import com.taskflow.api.dto.AtividadeResponseDTO;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Milestone;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final ProjetoRepository projetoRepository;

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
}