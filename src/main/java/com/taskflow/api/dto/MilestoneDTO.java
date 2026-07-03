package com.taskflow.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record MilestoneDTO (
        UUID idMilestone,
        String nomeMilestone,
        String descricao,
        LocalDate dataPrevista)
{}