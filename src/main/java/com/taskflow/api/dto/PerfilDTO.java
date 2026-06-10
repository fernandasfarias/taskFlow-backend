package com.taskflow.api.dto;

import com.taskflow.api.enums.TipoUsuario;

public record PerfilDTO (
    String nome,
    String email,
    String senha,
    TipoUsuario tipo
){};
