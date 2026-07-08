package com.taskflow.api.dto;
import java.util.UUID;

public record EtapaDTO(UUID idEtapa, String nomeEtapa, Boolean concluida) {}