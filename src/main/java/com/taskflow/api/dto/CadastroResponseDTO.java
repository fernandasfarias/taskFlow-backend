package com.taskflow.api.dto;

import com.taskflow.api.enums.TipoUsuario;

import java.util.UUID;

public record CadastroResponseDTO (
    UUID idManager,
    String nomeManager,
    String Email,
    TipoUsuario tipo
) {}
