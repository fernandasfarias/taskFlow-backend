package com.taskflow.api.dto;

import com.taskflow.api.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record AtividadeResponseDTO(
        UUID idAtividade,
        String nomeAtividade,
        String descricaoAtividade,
        LocalDate dataInicio,
        LocalDate dataEntrega,
        Status statusAtividade,
        UUID idProjeto,
        UUID idMilestone
) {}