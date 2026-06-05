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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroResponseDTO> cadastrar(@RequestBody @Valid CadastroDTO dto){
            return ResponseEntity.ok(service.cadastrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<CadastroResponseDTO> login(@RequestBody CadastroDTO dto){
        return ResponseEntity.ok(service.cadastrar(dto));
    }

}
