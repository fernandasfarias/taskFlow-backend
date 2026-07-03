package com.taskflow.api.controller;

import com.taskflow.api.dto.AssociarColaboradorAtividadeDTO;
import com.taskflow.api.dto.AtividadeRequestDTO;
import com.taskflow.api.dto.AtividadeResponseDTO;
import com.taskflow.api.dto.AtualizarAtividadeDTO;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.service.AtividadeService;
import com.taskflow.api.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;
    private final ProjetoService projetoService;

    @PostMapping
    public AtividadeResponseDTO criar(@RequestBody AtividadeRequestDTO dto) {
        return atividadeService.criar(dto);
    }

    @PostMapping("/associar-colaborador")
    public ResponseEntity<Void> associarColaborador(
            @RequestBody AssociarColaboradorAtividadeDTO dto
    ) {
        atividadeService.associarColaborador(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/colaboradores")
    public List<Colaborador> listarColaboradores() {
        return projetoService.listarTodosColaboradores();
    }

    @GetMapping("/projeto/{idProjeto}")
    public List<AtividadeResponseDTO> listarPorProjeto(@PathVariable UUID idProjeto) {
        return atividadeService.listarPorProjeto(idProjeto);
    }

    @GetMapping("/{idAtividade}")
    public AtividadeResponseDTO buscarPorId(@PathVariable UUID idAtividade) {
        return atividadeService.buscarPorId(idAtividade);
    }

    @DeleteMapping("/{idAtividade}")
    public void deletar(@PathVariable UUID idAtividade) {
        atividadeService.deletar(idAtividade);
    }

    @PutMapping("/{idAtividade}")
    public ResponseEntity<Void> atualizar(
            @PathVariable UUID idAtividade,
            @RequestBody AtualizarAtividadeDTO dto
    ) {
        atividadeService.atualizar(idAtividade, dto);
        return ResponseEntity.noContent().build();
    }

}