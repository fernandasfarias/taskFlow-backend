package com.taskflow.api.dto;
import com.taskflow.api.enums.TipoUsuario;
import jakarta.validation.Valid;

import java.util.List;

public record CadastroDTO(
    String nome,
    String email,
    String senha,
    TipoUsuario tipo
) {}