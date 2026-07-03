package com.taskflow.api.controller;

import com.taskflow.api.dto.AssociarColaboradorTarefaDTO;
import com.taskflow.api.dto.CriarTarefaDTO;
import com.taskflow.api.dto.TarefaRequestDTO;
import com.taskflow.api.dto.TarefaResponseDTO;
import com.taskflow.api.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping("/{idAtividade}/tarefas")
    public ResponseEntity<TarefaResponseDTO> criar(
            @PathVariable UUID idAtividade,
            @RequestBody CriarTarefaDTO dto
    ) {
        TarefaResponseDTO tarefa = tarefaService.criar(idAtividade, dto);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/deletar-tarefa/{idTarefa}")
    public ResponseEntity<Void> excluir(@PathVariable UUID idTarefa) {

        tarefaService.excluir(idTarefa);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-tarefa/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> buscar(@PathVariable UUID idTarefa) {
        return ResponseEntity.ok(tarefaService.buscar(idTarefa));
    }

    @PutMapping("/editar-tarefa/{idTarefa}")
    public ResponseEntity<TarefaResponseDTO> editar(
            @PathVariable UUID idTarefa,
            @RequestBody TarefaRequestDTO request
    ) {
        return ResponseEntity.ok(tarefaService.editar(idTarefa, request));
    }

    @PostMapping("/tarefa/associar-colaborador")
    public ResponseEntity<Void> associarColaborador(
            @RequestBody AssociarColaboradorTarefaDTO dto
    ) {
        tarefaService.associarColaborador(dto);
        return ResponseEntity.ok().build();
    }
}