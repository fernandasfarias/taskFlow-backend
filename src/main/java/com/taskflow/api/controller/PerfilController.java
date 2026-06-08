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
    // FUNCIONANDO
    @PostMapping("me/especialidade")
    public void adicionarEspecialidade(@AuthenticationPrincipal UsuarioAutenticadoDTO user, @RequestBody EspecialidadeDTO dto){
        especialidadeService.adicionarEspecialidade(user.email(), dto);
    }
    // remover especialidade
    // FUNCIONANDO
    @DeleteMapping("me/especialidade/{idEspecialidade}")
    public void removerEspecialidade(@AuthenticationPrincipal UsuarioAutenticadoDTO user, @PathVariable UUID idEspecialidade){
        especialidadeService.removerEspecialidade(user.email(), idEspecialidade);
    }

    // EMPRESA
    // adicionar nova empresa caso o cliente tenha removido a que ele criou durante o cadastro
    // FUNCIONANDO
    @PostMapping("/me/empresa")
    public void adicionarEmpresa(@AuthenticationPrincipal UsuarioAutenticadoDTO user, @RequestBody EmpresaDTO dto){
        empresaService.adicionarEmpresa(user.email(), dto);
    }
    // remover empresa
    // FUNCIONANDO
    @DeleteMapping("/me/empresa")
    public void removerEmpresa(@AuthenticationPrincipal UsuarioAutenticadoDTO user){
        empresaService.removerEmpresa(user.email());
    }
}
