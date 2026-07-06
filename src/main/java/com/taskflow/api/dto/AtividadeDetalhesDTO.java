package com.taskflow.api.dto;

import java.util.List;

public record AtividadeDetalhesDTO(
        AtividadeResponseDTO atividade,
        List<TarefaDTO> tarefas,
        MilestoneDTO milestone
) {}
