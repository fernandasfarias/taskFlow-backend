package com.taskflow.api.service;

import com.taskflow.api.dto.DashboardStatsDTO;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjetoRepository projetoRepository;

    public DashboardStatsDTO getStats(UUID idUsuarioLogado) {
        List<Projeto> projetos = projetoRepository.buscarProjetosDoUsuario(idUsuarioLogado);

        LocalDate hoje = LocalDate.now();

        long total = projetos.size();

        long concluidos = projetos.stream()
                .filter(p -> p.getDataEntrega() != null)
                .filter(p -> p.getDataEntrega().isBefore(hoje))
                .count();

        long emAndamento = projetos.stream()
                .filter(p -> p.getDataInicio() != null)
                .filter(p -> p.getDataEntrega() != null)
                .filter(p -> !hoje.isBefore(p.getDataInicio()) && !hoje.isAfter(p.getDataEntrega()))
                .count();

        long aFazer = projetos.stream()
                .filter(p -> p.getDataInicio() == null)
                .filter(p -> p.getDataEntrega() == null)
                .count();

        return new DashboardStatsDTO(total, emAndamento, concluidos, aFazer);
    }

    public List<Projeto> getProjects(UUID idUsuarioLogado) {
        return projetoRepository.buscarProjetosDoUsuario(idUsuarioLogado);
    }

    public List<Projeto> searchProjects(UUID idUsuarioLogado, String termo) {
        if (termo == null || termo.isBlank()) {
            return projetoRepository.buscarProjetosDoUsuario(idUsuarioLogado);
        }

        return projetoRepository.buscarProjetosDoUsuarioPorTermo(
                idUsuarioLogado,
                termo.trim()
        );
    }
}