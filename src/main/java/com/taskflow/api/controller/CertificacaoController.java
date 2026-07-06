package com.taskflow.api.controller;

import com.taskflow.api.service.CertificacaoService;
import com.taskflow.api.service.JwtService; 
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskflow.api.dto.CertificacaoDTO;
import com.taskflow.api.entity.ProjectManager;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/onboarding/certificacoes")
public class CertificacaoController {

    private final CertificacaoService certificacaoService;
    private final JwtService jwtService;

    @PostMapping
    public void adicionarCertificacoes(@RequestHeader("Authorization") String authHeader, @RequestBody List<CertificacaoDTO> dtos) {
        String token = authHeader.substring(7);
        String IdExtraido = jwtService.extrairEmail(token);
        UUID idManager = UUID.fromString(IdExtraido);

        certificacaoService.adicionar(idManager, dtos);

    }

    {/* controller para remover certificação */}
    @DeleteMapping("/{idCertificacao}")
    public ResponseEntity<Void> removerCertificacao(@PathVariable UUID idCertificacao){
        certificacaoService.remover(idCertificacao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<List<CertificacaoDTO>> listarCertificacoes(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(certificacaoService.listarPorEmail(email));
    }
}
