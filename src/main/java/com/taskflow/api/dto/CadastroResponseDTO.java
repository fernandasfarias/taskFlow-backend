package com.taskflow.api.dto;

import java.util.UUID;

public record CadastroResponseDTO (
    UUID idManager,
    String nomeManager,
    String Email
) {}
