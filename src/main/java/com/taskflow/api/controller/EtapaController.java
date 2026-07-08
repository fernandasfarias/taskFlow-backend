package com.taskflow.api.controller;

import com.taskflow.api.dto.EtapaDTO;
import com.taskflow.api.dto.MilestoneDTO;
import com.taskflow.api.service.MilestoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/etapas")

public class EtapaController {

    private final MilestoneService milestoneService;

    public EtapaController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @PatchMapping("/{idEtapa}/toggle")
    public ResponseEntity<MilestoneDTO> alternarStatus(@NonNull @PathVariable UUID idEtapa) {
        return ResponseEntity.ok(milestoneService.alternarStatusEtapa(idEtapa));
    }

    @PutMapping("/{idEtapa}")
    public ResponseEntity<MilestoneDTO> editarEtapa(
            @NonNull @PathVariable UUID idEtapa, 
            @RequestBody EtapaDTO dto) {
        return ResponseEntity.ok(milestoneService.atualizarEtapa(idEtapa, dto.nomeEtapa()));
    }

    @DeleteMapping("/{idEtapa}")
    public ResponseEntity<MilestoneDTO> deletarEtapa(@NonNull @PathVariable UUID idEtapa) {
        return ResponseEntity.ok(milestoneService.deletarEtapa(idEtapa));
    }
}