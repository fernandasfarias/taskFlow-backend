package com.taskflow.api.service;

import com.taskflow.api.dto.EtapaDTO;
import com.taskflow.api.dto.MilestoneDTO;
import com.taskflow.api.entity.Atividade;
import com.taskflow.api.entity.Etapa;
import com.taskflow.api.entity.Milestone;
import com.taskflow.api.repository.AtividadeRepository;
import com.taskflow.api.repository.EtapaRepository;
import com.taskflow.api.repository.MilestoneRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final AtividadeRepository atividadeRepository;
    private final EtapaRepository etapaRepository;

    public MilestoneService(MilestoneRepository milestoneRepository, AtividadeRepository atividadeRepository, EtapaRepository etapaRepository) {
        this.milestoneRepository = milestoneRepository;
        this.atividadeRepository = atividadeRepository;
        this.etapaRepository = etapaRepository;
    }

    @Transactional
    public MilestoneDTO criarMilestone(@NonNull UUID idAtividade, MilestoneDTO dto) {
        Atividade atividade = atividadeRepository.findById(idAtividade)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));

        Milestone milestone = new Milestone();
        milestone.setNomeMilestone(dto.nomeMilestone());
        milestone.setDescricao(dto.descricao());
        milestone.setDataPrevista(dto.dataPrevista());

        if (dto.etapas() != null) {
            List<Etapa> etapas = dto.etapas().stream().map(e -> {
                Etapa etapa = new Etapa();
                etapa.setNomeEtapa(e.nomeEtapa());
                etapa.setConcluida(e.concluida() != null ? e.concluida() : false);
                etapa.setMilestone(milestone); 
                return etapa;
            }).collect(Collectors.toList());
            milestone.setEtapas(etapas);
        }

        Milestone salva = milestoneRepository.save(milestone);
        atividade.setMilestone(salva);
        atividadeRepository.save(atividade);

        return converterParaDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<MilestoneDTO> listarPorAtividade(@NonNull UUID idAtividade) {
        return milestoneRepository.findByAtividade_IdAtividade(idAtividade)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MilestoneDTO alternarStatusEtapa(@NonNull UUID idEtapa) {
        Etapa etapa = etapaRepository.findById(idEtapa)
                .orElseThrow(() -> new RuntimeException("Etapa não encontrada"));
        
        etapa.setConcluida(!etapa.getConcluida());
        etapaRepository.save(etapa);

        Milestone m = etapa.getMilestone();
        if(m == null) throw new RuntimeException("Milestone não encontrado na etapa");

        return converterParaDTO(m);
    }

    public MilestoneDTO converterParaDTO(@NonNull Milestone m) {
        List<Etapa> listaEtapas = m.getEtapas();
        if (listaEtapas == null) {
            listaEtapas = List.of();
        }

        int total = listaEtapas.size();
        int concluidas = (int) listaEtapas.stream()
                .filter(e -> e != null && Boolean.TRUE.equals(e.getConcluida()))
                .count();
        
        int progresso = (total == 0) ? 0 : (concluidas * 100) / total;

        List<EtapaDTO> etapasDTO = listaEtapas.stream()
                .filter(e -> e != null)
                .map(e -> new EtapaDTO(e.getIdEtapa(), e.getNomeEtapa(), e.getConcluida()))
                .collect(Collectors.toList());

        return new MilestoneDTO(
                m.getIdMilestone(),
                m.getNomeMilestone(),
                m.getDescricao(),
                m.getDataPrevista(),
                etapasDTO,
                progresso
        );
    }

    @Transactional
    public void deletarMilestone(@NonNull UUID idMilestone) {
        if (!milestoneRepository.existsById(idMilestone)) {
            throw new RuntimeException("Milestone não encontrado.");
        }
        milestoneRepository.deleteById(idMilestone);
    }

    @Transactional
    public MilestoneDTO atualizarMilestone(@NonNull UUID idMilestone, MilestoneDTO dto) {
        Milestone milestone = milestoneRepository.findById(idMilestone)
                .orElseThrow(() -> new RuntimeException("Milestone não encontrado."));

        // Atualiza os dados básicos
        milestone.setNomeMilestone(dto.nomeMilestone());
        milestone.setDescricao(dto.descricao());
        milestone.setDataPrevista(dto.dataPrevista());

        // 1. Limpa as etapas antigas do banco de forma segura
        if (milestone.getEtapas() != null && !milestone.getEtapas().isEmpty()) {
            // Em vez de deleteAll com casting, usamos o próprio repositório para remover cada uma
            // ou limpamos a associação antes de deletar. 
            // A forma mais garantida para o compilador do Java:
            milestone.getEtapas().forEach(etapaRepository::delete);
            milestone.getEtapas().clear();
        }

        // 2. Salva a nova lista exata de etapas que você editou no React
        if (dto.etapas() != null && !dto.etapas().isEmpty()) {
            for (EtapaDTO eDto : dto.etapas()) {
                Etapa novaEtapa = new Etapa();
                novaEtapa.setNomeEtapa(eDto.nomeEtapa());
                novaEtapa.setConcluida(eDto.concluida() != null ? eDto.concluida() : false);
                novaEtapa.setMilestone(milestone);
                
                milestone.getEtapas().add(novaEtapa);
            }
        }

        Milestone salva = milestoneRepository.save(milestone);
        return converterParaDTO(salva);
    }

    @Transactional
    public MilestoneDTO atualizarEtapa(@NonNull UUID idEtapa, String novoNome) {
        Etapa etapa = etapaRepository.findById(idEtapa)
                .orElseThrow(() -> new RuntimeException("Etapa não encontrada."));
        
        etapa.setNomeEtapa(novoNome);
        etapaRepository.save(etapa);
        
        Milestone m = etapa.getMilestone();
        if (m == null) throw new RuntimeException("Milestone não encontrado na etapa"); 
        
        return converterParaDTO(m);
    }

    @Transactional
    public MilestoneDTO deletarEtapa(@NonNull UUID idEtapa) {
        Etapa etapa = etapaRepository.findById(idEtapa)
                .orElseThrow(() -> new RuntimeException("Etapa não encontrada."));
        
        Milestone m = etapa.getMilestone();
        if (m == null) throw new RuntimeException("Milestone não encontrado na etapa"); 
        
        etapaRepository.delete(etapa);
        
        if (m.getEtapas() != null) {
            m.getEtapas().remove(etapa); 
        }
        
        return converterParaDTO(m);
    }
}