package com.taskflow.api.controller;

import com.taskflow.api.dto.AlterarStatusDTO;
import com.taskflow.api.dto.AtividadeDetalhesDTO;
import com.taskflow.api.dto.KanbanAtividadeDTO;
import com.taskflow.api.service.AtividadeService;
import com.taskflow.api.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projetos/{idProjeto}/kanban")
public class KanbanController {

    private final KanbanService kanbanService;

    @Autowired
    private AtividadeService atividadeService;

    public KanbanController(KanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }

    @GetMapping
    public List<KanbanAtividadeDTO> listar(@PathVariable UUID idProjeto) {
        return kanbanService.listarPorProjeto(idProjeto);
    }

    @PatchMapping("/atividades/{idAtividade}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable UUID idProjeto,
            @PathVariable UUID idAtividade,
            @RequestBody AlterarStatusDTO dto
    ) {
        kanbanService.alterarStatus(idAtividade, dto.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/atividades/{idAtividade}/excluir")
    public void deletarAtividade(@PathVariable UUID idAtividade) {
        atividadeService.deletar(idAtividade);
    }

    @GetMapping("/{idAtividade}/detalhes")
    public ResponseEntity<AtividadeDetalhesDTO> detalhes(
            @PathVariable UUID idAtividade
    ) {
        return ResponseEntity.ok(
                atividadeService.buscarDetalhes(idAtividade)
        );
    }
}