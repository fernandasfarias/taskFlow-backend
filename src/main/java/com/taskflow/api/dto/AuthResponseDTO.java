package com.taskflow.api.dto;

public record AuthResponseDTO(
    String token,
    PerfilDTO usuario
){}
