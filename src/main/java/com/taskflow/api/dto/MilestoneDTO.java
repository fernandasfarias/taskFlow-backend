package com.taskflow.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MilestoneDTO (
        UUID idMilestone,
        String nomeMilestone,
        String descricao,
        LocalDate dataPrevista,
        List<EtapaDTO> etapas, 
        Integer progresso
) {}