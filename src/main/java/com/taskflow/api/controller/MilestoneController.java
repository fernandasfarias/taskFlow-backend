package com.taskflow.api.controller;

import com.taskflow.api.dto.MilestoneDTO;
import com.taskflow.api.service.MilestoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/milestones")

public class MilestoneController {

    private final MilestoneService milestoneService;

    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @PostMapping("/atividade/{idAtividade}")
    public ResponseEntity<MilestoneDTO> criarMilestone(
            @NonNull @PathVariable UUID idAtividade, 
            @RequestBody MilestoneDTO dto) {
        return ResponseEntity.ok(milestoneService.criarMilestone(idAtividade, dto));
    }

    @GetMapping("/atividade/{idAtividade}")
    public ResponseEntity<List<MilestoneDTO>> listarPorAtividade(@NonNull @PathVariable UUID idAtividade) {
        return ResponseEntity.ok(milestoneService.listarPorAtividade(idAtividade));
    }

    @PutMapping("/{idMilestone}")
    public ResponseEntity<MilestoneDTO> atualizar(
            @NonNull @PathVariable UUID idMilestone, 
            @RequestBody MilestoneDTO dto) {
        return ResponseEntity.ok(milestoneService.atualizarMilestone(idMilestone, dto));
    }

    @DeleteMapping("/{idMilestone}")
    public ResponseEntity<Void> deletar(@NonNull @PathVariable UUID idMilestone) {
        milestoneService.deletarMilestone(idMilestone);
        return ResponseEntity.noContent().build();
    }
}