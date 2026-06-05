package com.taskflow.api.dto;

import jakarta.validation.constraints.Pattern;

public record EmpresaDTO(
        String nome,
        @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "CNPJ inválido")
        String cnpj
) {}