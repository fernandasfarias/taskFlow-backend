package com.taskflow.api.controller;

import com.taskflow.api.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.taskflow.api.dto.EmpresaDTO;

import java.util.UUID;
@RestController
@RequestMapping("/onboarding/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping("/{idCliente}")
    public void adicionarEmpresa(
            @PathVariable UUID idCliente,
            @RequestBody EmpresaDTO dto
    ) {
        empresaService.vincularEmpresaCadastro(idCliente, dto);
    }
}
