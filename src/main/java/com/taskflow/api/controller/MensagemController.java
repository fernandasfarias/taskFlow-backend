package com.taskflow.api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.taskflow.api.dto.MensagemRequestDTO;
import com.taskflow.api.dto.MensagemResponseDTO;
import com.taskflow.api.entity.Mensagem;
import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.service.JwtService;
import com.taskflow.api.service.MensagemService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;
    private final JwtService jwtService;

    // Converte a Entidade complexa em um DTO limpo para evitar o loop infinito
    private MensagemResponseDTO converterParaDTO(Mensagem m) {
        String email = "";
        if (m.getProjectManager() != null) {
            email = m.getProjectManager().getEmail();
        } else if (m.getCliente() != null) {
            email = m.getCliente().getEmail();
        }
        return new MensagemResponseDTO(m.getIdMensagem(), m.getConteudo(), m.getDataHora(), email);
    }

    @PostMapping
    public ResponseEntity<MensagemResponseDTO> enviarMensagem(@RequestBody MensagemRequestDTO mensagemRequest, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");   
        String email = jwtService.extrairEmail(token);
        String roleString = jwtService.extrairRole(token);
        TipoUsuario role = TipoUsuario.valueOf(roleString);

        Mensagem mensagemSalva = mensagemService.enviarMensagem(mensagemRequest, email, role);

        return ResponseEntity.ok(converterParaDTO(mensagemSalva));
    }
    
    @GetMapping("/{idProjeto}")
    public ResponseEntity<List<MensagemResponseDTO>> listarMensagens(@PathVariable UUID idProjeto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extrairEmail(token);
        String roleString = jwtService.extrairRole(token);
        TipoUsuario role = TipoUsuario.valueOf(roleString);

        List<Mensagem> mensagens = mensagemService.listarMensagensPorProjeto(idProjeto, email, role);
        
        // Mapeia a lista de mensagens para lista de DTOs
        List<MensagemResponseDTO> dtos = mensagens.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}