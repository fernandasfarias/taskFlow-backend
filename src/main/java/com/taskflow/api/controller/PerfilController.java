package com.taskflow.api.controller;

import com.taskflow.api.dto.AtualizarCertificacoesDTO;
import com.taskflow.api.dto.AtualizarPerfilDTO;
import com.taskflow.api.dto.CertificacaoDTO;
import com.taskflow.api.dto.EmpresaDTO;
import com.taskflow.api.service.CertificacaoService;
import com.taskflow.api.service.EmpresaService;
import com.taskflow.api.service.EspecialidadeService;
import com.taskflow.api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;
    private final CertificacaoService certificacaoService;
    private final EmpresaService empresaService;
    private final EspecialidadeService especialidadeService;

    // editar o perfil do usuario: enpoint profile/id
    @PutMapping("{id}")
    public void atualizarPerfil(@PathVariable UUID id, @RequestBody AtualizarPerfilDTO dto){
        perfilService.atualizar(id, dto);
    }
    @DeleteMapping("{id}")
    public void removerPerfil(@PathVariable UUID id){
        perfilService.removerPerfil(id);
    }

    // CERTIFICACAO
    // adicionar nova certificacao
    @PostMapping("{id}/certificacoes")
    public void adicionarCertificacao(@PathVariable UUID id, @RequestBody CertificacaoDTO dto){
        certificacaoService.adicionarCertificacao(id, dto);
    }
    //editar certificacao
    @PutMapping("{id}/certificacoes/{idCertificacao}")
    public void atualizarCertificacoes(@PathVariable UUID id, @RequestBody AtualizarCertificacoesDTO dto){
        certificacaoService.atualizar(id, dto);
    }
    // remover certificacao
    @DeleteMapping("{id}/certificacoes/{idCertificacao}")
    public void removerCertificacao(@PathVariable UUID idCertificacao){
        certificacaoService.remover(idCertificacao);
    }

    //ESPECIALIDADE
    // adicionar especialidade
    @PostMapping("{id}/especialidade/{idEspecialidade}")
    public void adicionarEspecialidade(@PathVariable UUID id, @PathVariable UUID idEspecialidade){
        especialidadeService.adicionarEspecialidade(id, idEspecialidade);
    }
    // remover especialidade
    @DeleteMapping("{id}/especialidade/{idEspecialidade}")
    public void removerEspecialidade(@PathVariable UUID id, @PathVariable UUID idEspecialidade){
        especialidadeService.removerEspecialidade(id, idEspecialidade);
    }

    // EMPRESA
    // vincular empresa
    @PutMapping("{id}/empresa")
    public void vincularEmpresa(@PathVariable UUID id, @RequestBody EmpresaDTO dto){
        empresaService.vincular(id, dto);
    }
    // remover empresa
    @DeleteMapping("{id}/empresa")
    public void removerEmpresa(@PathVariable UUID id){
        empresaService.removerEmpresa(id);
    }
}
