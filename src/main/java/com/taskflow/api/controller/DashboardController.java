package com.taskflow.api.controller;

import com.taskflow.api.dto.DashboardStatsDTO;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardStatsDTO getStats(Authentication authentication) {
        UUID idUsuarioLogado = UUID.fromString(authentication.getName());
        return dashboardService.getStats(idUsuarioLogado);
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(Authentication authentication) {
        return Map.of(
                "name", authentication.getName(),
                "role", authentication.getAuthorities().toString(),
                "avatarUrl", ""
        );
    }

    @GetMapping("/projects")
    public List<Projeto> getProjects(
            Authentication authentication,
            @RequestParam(required = false) String search
    ) {
        UUID idUsuarioLogado = UUID.fromString(authentication.getName());

        if (search != null && !search.isBlank()) {
            return dashboardService.searchProjects(idUsuarioLogado, search);
        }

        return dashboardService.getProjects(idUsuarioLogado);
    }
}