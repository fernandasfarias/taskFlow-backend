package com.taskflow.api.controller;

import com.taskflow.api.service.CertificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.taskflow.api.dto.CertificacaoDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding/certificacoes")
public class CertificacaoController {
    private final CertificacaoService certificacaoService;
    @PostMapping("/{idManager}")
    public void adicionarCertificacoes(@PathVariable UUID idManager, @RequestBody List<CertificacaoDTO> dtos) {
        certificacaoService.adicionar(idManager, dtos);
    }
}
