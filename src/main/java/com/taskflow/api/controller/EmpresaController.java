package com.taskflow.api.controller;

import com.taskflow.api.service.EmpresaService;
import com.taskflow.api.service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.taskflow.api.dto.EmpresaDTO;

import java.util.UUID;
@RestController
@RequestMapping("/onboarding/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;
    private final JwtService jwtService;

    @PostMapping
    public void vincularEmpresaCadastro(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid EmpresaDTO dto) {

        // removeme os 7 primeiros caracteres
        String token = authHeader.substring(7);
        
        // extrair o ID do subject do token
        String idExtraido = jwtService.extrairEmail(token);
        UUID idCliente = UUID.fromString(idExtraido);

        empresaService.vincularEmpresaCadastro(idCliente, dto);
    }

}
