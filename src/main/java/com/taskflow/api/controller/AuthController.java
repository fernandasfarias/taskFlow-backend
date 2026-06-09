package com.taskflow.api.controller;

import com.taskflow.api.dto.*;
import com.taskflow.api.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@RequestBody @Valid CadastroDTO dto){
            return ResponseEntity.ok(service.cadastrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto){
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> solicitarRecuperacaoSenha(
            @RequestBody @Valid RecuperarSenhaDTO recuperarSenhaDTO
    ) {
        return ResponseEntity.ok(service.solicitarRecuperacaoSenha(recuperarSenhaDTO.email()));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(
            @RequestBody @Valid RedefinirSenhaDTO redefinirSenhaDTO
    ) {
        return ResponseEntity.ok(service.redefinirSenha(redefinirSenhaDTO.token(), redefinirSenhaDTO.novaSenha()));
    }

}
