package com.taskflow.api.controller;

import com.taskflow.api.dto.EspecialidadeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import  com.taskflow.api.service.EspecialidadeService;

@RestController
@RequestMapping("/onboarding/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {
    private final EspecialidadeService especialidadeService;

    @PostMapping("/{idColaborador}")
    public void adicionar(
            @PathVariable UUID idColaborador,
            @RequestBody List<EspecialidadeDTO> dtos
    ){especialidadeService.adicionar(idColaborador, dtos);}
}
