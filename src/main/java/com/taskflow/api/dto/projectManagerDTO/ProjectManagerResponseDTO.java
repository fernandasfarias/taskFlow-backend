package com.taskflow.api.dto.projectManagerDTO;

import java.util.UUID;

public record ProjectManagerResponseDTO (
    UUID idManager,
    String nomeManager,
    String Email
) {}
