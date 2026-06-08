package com.taskflow.api.dto;
import java.util.UUID;

public record MensagemRequestDTO(
    String conteudo,
    UUID idProjeto
) {}