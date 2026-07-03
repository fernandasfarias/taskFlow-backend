package com.taskflow.api.dto;

import java.util.UUID;

public record AssociarColaboradorTarefaDTO(
        UUID idTarefa,
        UUID idColaborador
) {}
