package com.taskflow.api.dto;

public record DashboardStatsDTO(
        long total,
        long emAndamento,
        long concluidos,
        long aFazer
) {}
