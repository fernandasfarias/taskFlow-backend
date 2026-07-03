package com.taskflow.api.dto;

import com.taskflow.api.enums.Status;

import java.time.LocalDate;
import java.util.UUID;

public record TarefaResponseDTO(
        UUID idTarefa,
        String nomeTarefa,
        LocalDate dataInicio,
        LocalDate dataEntrega,
        Status statusTarefa
) {}