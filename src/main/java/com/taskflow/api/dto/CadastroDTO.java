package com.taskflow.api.dto;
import com.taskflow.api.enums.TipoUsuario;
import jakarta.validation.Valid;

import java.util.List;

public record CadastroDTO(
    String nome,
    String email,
    String senha,
    TipoUsuario tipo,

    @Valid
    EmpresaDTO empresa,

    @Valid
    List<CertificacaoDTO> certificacoes,

    @Valid
    List<EspecialidadeDTO> especialidades
) {}