package com.taskflow.api.controller;

import com.taskflow.api.dto.AtividadeRequestDTO;
import com.taskflow.api.dto.AtividadeResponseDTO;
import com.taskflow.api.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @PostMapping
    public AtividadeResponseDTO criar(@RequestBody AtividadeRequestDTO dto) {
        return atividadeService.criar(dto);
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
}