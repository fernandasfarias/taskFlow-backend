package com.taskflow.api.controller;

import com.taskflow.api.dto.CriarTarefaDTO;
import com.taskflow.api.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping("/{idAtividade}/tarefas")
    public ResponseEntity<Void> criar(
            @PathVariable UUID idAtividade,
            @RequestBody CriarTarefaDTO dto
    ) {
        tarefaService.criar(idAtividade, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar-tarefa/{idTarefa}")
    public ResponseEntity<Void> excluir(@PathVariable UUID idTarefa) {

        tarefaService.excluir(idTarefa);

        return ResponseEntity.noContent().build();
    }

}