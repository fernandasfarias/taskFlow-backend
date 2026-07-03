package com.taskflow.api.dto;

import com.taskflow.api.enums.Status;

import java.time.LocalDate;

public record TarefaRequestDTO(
        String nomeTarefa,
        LocalDate dataInicio,
        LocalDate dataEntrega,
        Status statusTarefa
) {}
