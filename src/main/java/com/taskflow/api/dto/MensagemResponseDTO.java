package com.taskflow.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemResponseDTO(
    UUID idMensagem,
    String conteudo,
    LocalDateTime dataHora,
    UUID remetenteId, 
    String remetenteNome
) {}