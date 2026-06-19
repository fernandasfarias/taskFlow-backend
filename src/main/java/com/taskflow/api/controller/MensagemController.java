package com.taskflow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskflow.api.dto.MensagemRequestDTO;
import com.taskflow.api.entity.Mensagem;
import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.service.JwtService;
import com.taskflow.api.service.MensagemService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/api/mensagens")
@RequiredArgsConstructor


public class MensagemController {

    private final MensagemService mensagemService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Mensagem> enviarMensagem(@RequestBody MensagemRequestDTO mensagemRequest, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", ""); // Remove o prefixo "Bearer " do token   


        String email = jwtService.extrairEmail(token);
        String roleString = jwtService.extrairRole(token);
        TipoUsuario role = TipoUsuario.valueOf(roleString);

        Mensagem mensagemSalva = mensagemService.enviarMensagem(mensagemRequest, email, role);

        return ResponseEntity.ok(mensagemSalva);
    }
    
    
}
