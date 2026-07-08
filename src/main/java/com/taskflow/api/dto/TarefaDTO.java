package com.taskflow.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TarefaDTO(
        UUID idTarefa,
        UUID idAtividade,
        String nomeTarefa,
        LocalDate dataInicio,
        LocalDate dataEntrega,
        String statusTarefa
) {}