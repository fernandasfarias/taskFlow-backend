package com.taskflow.api.dto;

import com.taskflow.api.enums.TipoUsuario;

import java.util.UUID;

public record UsuarioAutenticadoDTO(
        UUID id,
        String nome,
        String email,
        String senha,
        TipoUsuario tipo
) {}
