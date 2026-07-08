package com.taskflow.api.controller;

import com.taskflow.api.dto.*;
import com.taskflow.api.service.CertificacaoService;
import com.taskflow.api.service.EmpresaService;
import com.taskflow.api.service.EspecialidadeService;
import com.taskflow.api.service.JwtService;
import com.taskflow.api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;

import com.taskflow.api.service.JwtService; 

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;
    private final CertificacaoService certificacaoService;
    private final EmpresaService empresaService;
    private final EspecialidadeService especialidadeService;
    private final JwtService jwtService;

    // PERFIL DO USUARIO
    // editar o perfil do usuario: enpoint profile/id
    // FUNCIONANDO
    @PutMapping("/me")
    public void atualizarPerfil(Authentication authentication, @RequestBody AtualizarPerfilDTO dto){
        UUID id = UUID.fromString(authentication.getName());
        perfilService.atualizar(id, dto);
    }
    // deleter o perfil do usuario
    // FUNCIONANDO
    @DeleteMapping("/me")
    public void removerPerfil(Authentication authentication){
        UUID id = UUID.fromString(authentication.getName());
        perfilService.removerPerfil(id);
    }
    // buscar dados do perfil do usuario
    //FUNCIONANDO
    @GetMapping("/me")
    public PerfilDTO getPerfil(Authentication authentication){
        String id = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return perfilService.buscarPerfil(UUID.fromString(id), role);
    }

    // CERTIFICACAO
    // remover certificacao
    // FUNCIONANDO !
    @DeleteMapping("/me/certificacoes/{id}")
    public void removerCertificacao(@PathVariable UUID id){
        certificacaoService.remover(id);
    }
    // listar certificações
    // FUNCIONANDO !
    @GetMapping("/me/certificacoes")
    public List<CertificacaoDTO> listarCertificacoes(@RequestHeader("Authorization") String authReader){
        String token = authReader.substring(7);
        String idExtraido = jwtService.extrairEmail(token);
        UUID idManager = UUID.fromString(idExtraido);
        return certificacaoService.listarPorId(idManager);
    }

    //ESPECIALIDADE
    // listar especialidades
    // FUNCIONANDO !
    @GetMapping("/me/especialidades")
    public List<EspecialidadeDTO> listarEspecialidades(@RequestHeader("Authorization") String authReader){
        String token = authReader.substring(7);
        String idExtraido = jwtService.extrairEmail(token);
        UUID idColaborador = UUID.fromString(idExtraido);
        return especialidadeService.listarEspecialidades(idColaborador);
    }

    // EMPRESA
    // listar empresa
    // FUNCIONANDO !
    @GetMapping("/me/empresa")
    public EmpresaDTO listarEmpresa(@RequestHeader("Authorization") String authReader){
        String token = authReader.substring(7);
        String idExtraido = jwtService.extrairEmail(token);
        UUID idCliente = UUID.fromString(idExtraido);
        return empresaService.listarEmpresa(idCliente);
    }
}
