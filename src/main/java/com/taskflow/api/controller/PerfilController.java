package com.taskflow.api.controller;

import com.taskflow.api.dto.*;
import com.taskflow.api.service.CertificacaoService;
import com.taskflow.api.service.EmpresaService;
import com.taskflow.api.service.EspecialidadeService;
import com.taskflow.api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;
    private final CertificacaoService certificacaoService;
    private final EmpresaService empresaService;
    private final EspecialidadeService especialidadeService;

    // editar o perfil do usuario: enpoint profile/id
    // FUNCIONANDO
    @PutMapping("/me")
    public void atualizarPerfil(@AuthenticationPrincipal UsuarioAutenticadoDTO usuario, @RequestBody AtualizarPerfilDTO dto){
        perfilService.atualizar(usuario.email(), dto);
    }
    // FUNCIONANDO
    @DeleteMapping("/me")
    public void removerPerfil(@AuthenticationPrincipal UsuarioAutenticadoDTO usuarioAutenticadoDTO){
        perfilService.removerPerfil(usuarioAutenticadoDTO.email());
    }

    // CERTIFICACAO
    // adicionar nova certificacao
    // FUNCIONANDO
    @PostMapping("/me/certificacoes")
    public void adicionarCertificacao(@AuthenticationPrincipal UsuarioAutenticadoDTO user, @RequestBody CertificacaoDTO dto){
        certificacaoService.adicionarCertificacao(user.email(), dto);
    }
    //editar certificacao
    // FUNCIONANDO
    @PutMapping("/me/certificacoes/{id}")
    public void atualizarCertificacoes(@PathVariable("id") UUID id, @RequestBody CertificacaoDTO dto){
        certificacaoService.atualizar(id, dto);
    }
    // remover certificacao
    // FUNCIONANDO
    @DeleteMapping("/me/certificacoes/{id}")
    public void removerCertificacao(@PathVariable UUID id){
        certificacaoService.remover(id);
    }

    /*CRIAR COLABORADOR E CRIAR CLIENTE PARA TESTAR ESPECIALIDADE E TESTAR EMPRESA*/
    //ESPECIALIDADE
    // adicionar especialidade
    @PostMapping("me/especialidade/{idEspecialidade}")
    public void adicionarEspecialidade(Authentication authentication, @PathVariable UUID idEspecialidade){
        especialidadeService.adicionarEspecialidade(authentication.getName(), idEspecialidade);
    }
    // remover especialidade
    @DeleteMapping("me/especialidade/{idEspecialidade}")
    public void removerEspecialidade(Authentication authentication, @PathVariable UUID idEspecialidade){
        especialidadeService.removerEspecialidade(authentication.getName(), idEspecialidade);
    }

    // EMPRESA
    // vincular empresa
    @PutMapping("me/empresa")
    public void vincularEmpresa(Authentication authentication, @RequestBody EmpresaDTO dto){
        empresaService.vincularEmpresa(authentication.getName(), dto);
    }
    // remover empresa
    @DeleteMapping("me/empresa")
    public void removerEmpresa(Authentication authentication){
        empresaService.removerEmpresa(authentication.getName());
    }
}
