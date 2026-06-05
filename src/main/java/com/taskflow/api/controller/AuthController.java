package com.taskflow.api.controller;

import com.taskflow.api.dto.CadastroDTO;
import com.taskflow.api.dto.CadastroResponseDTO;
import com.taskflow.api.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

import com.taskflow.api.dto.LoginResponseDTO;
import com.taskflow.api.dto.LoginDTO;

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

}
