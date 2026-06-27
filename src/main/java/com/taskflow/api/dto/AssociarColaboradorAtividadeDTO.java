package com.taskflow.api.dto;

import java.util.UUID;

public record AssociarColaboradorAtividadeDTO(
        UUID idAtividade,
        UUID idColaborador
) {}