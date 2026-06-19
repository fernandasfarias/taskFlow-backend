package com.taskflow.api.controller;

import com.taskflow.api.dto.EspecialidadeDTO;
import com.taskflow.api.service.EspecialidadeService;
import com.taskflow.api.service.JwtService; 
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/onboarding/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {
    
    private final EspecialidadeService especialidadeService;
    private final JwtService jwtService;

    @PostMapping
    public void adicionar(
            @RequestHeader("Authorization") String authHeader, 
            @RequestBody List<EspecialidadeDTO> dtos) {
        
        String token = authHeader.substring(7);
        String idExtraido = jwtService.extrairEmail(token);
        UUID idColaborador = UUID.fromString(idExtraido);

        especialidadeService.adicionar(idColaborador, dtos);
    }
}