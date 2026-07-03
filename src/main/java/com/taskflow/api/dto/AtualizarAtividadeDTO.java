package com.taskflow.api.dto;

import com.taskflow.api.enums.Status;

import java.time.LocalDate;

public record AtualizarAtividadeDTO (
        String nomeAtividade,
        String descricaoAtividade,
        LocalDate dataInicio,
        LocalDate dataEntrega,
        Status statusAtividade)
{}
