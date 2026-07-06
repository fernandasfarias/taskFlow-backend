package com.taskflow.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record KanbanAtividadeDTO (
    UUID id,
    String titulo,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataEntrega,
    String status,
    List<String> responsaveis
){}
