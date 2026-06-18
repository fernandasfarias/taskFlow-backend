package com.taskflow.api.controller;

import com.taskflow.api.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.taskflow.api.dto.ProjetoDTO;
import com.taskflow.api.entity.Cliente;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.entity.Projeto;
import java.util.Collections;
import org.springframework.security.core.Authentication;

import com.taskflow.api.dto.CertificacaoDTO;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {
    private final ProjetoService projetoService;

    //editar projeto
    // FUNCIONANDO
     @PutMapping("/{id}")
    public void atualizarProjeto(@PathVariable UUID id, @RequestBody ProjetoDTO dto, Authentication authentication) {
        // Pega o ID do gerente conectado pelo Token
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        // Passa o ID do projeto, os dados e o ID do dono para validação
        projetoService.editarProjeto(id, dto, idManagerLogado);
    }
 
    //deletar projeto
    // FUNCIONANDO
    @DeleteMapping("/{id}")
    public void deletarProjeto(@PathVariable UUID id, Authentication authentication){
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        projetoService.deletarProjeto(id, idManagerLogado);
    }

    //buscar projeto por nome
    // FUNCIONANDO
    @GetMapping("/buscar")
    public ProjetoDTO buscarPorNome(@RequestParam String nome) {
    // 1. O service busca a entidade Projeto pura do banco
    Projeto projetoEncontrado = projetoService.mostrarProjetoPorNome(nome);
    
    // 2. Você transforma o Projeto em ProjetoDTO mapeando os campos manualmente
    return new ProjetoDTO(
        projetoEncontrado.getId(),
        projetoEncontrado.getNome(),
        projetoEncontrado.getDescricao(),
        projetoEncontrado.getDataInicio(),
        projetoEncontrado.getDataEntrega(),
        projetoEncontrado.getOrcamento(),
        projetoEncontrado.getIdManager(),
        Collections.<UUID>emptyList(),
        Collections.<UUID>emptyList()
    );
    }

    // FUNCIONANDO
    @GetMapping
    public List<ProjetoDTO> listarTodosMeusProjetos(Authentication authentication) {
    //ID do gerente logado
    UUID idManagerLogado = UUID.fromString(authentication.getName());
    
    // lista de entidades do banco filtrada pelo projectmanager logado
    List<Projeto> meusProjetos = projetoService.listarProjetos(idManagerLogado);
    
    //converte a lista de entidades para uma lista de DTOs
    return meusProjetos.stream()
            .map(projeto -> new ProjetoDTO(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getDataInicio(),
                projeto.getDataEntrega(),
                projeto.getOrcamento(),
                projeto.getIdManager(),
                //Collections.<UUID>emptyList(), 
                projeto.getClientes().stream().map(Cliente::getIdCliente).toList(),
                //Collections.<UUID>emptyList()  
                projeto.getColaboradores().stream().map(Colaborador::getIdColaborador).toList()
            ))
            .toList();
     }

     //buscar projeto por id
     // FUNCIONANDO 
    @GetMapping("/{id}")
    public ProjetoDTO buscarPorId(@PathVariable UUID id) {
    Projeto projetoEncontrado = projetoService.mostrarProjetoPorId(id);
    return new ProjetoDTO(
        projetoEncontrado.getId(),
        projetoEncontrado.getNome(),
        projetoEncontrado.getDescricao(),
        projetoEncontrado.getDataInicio(),
        projetoEncontrado.getDataEntrega(),
        projetoEncontrado.getOrcamento(),
        projetoEncontrado.getIdManager(),
        projetoEncontrado.getClientes().stream().map(Cliente::getIdCliente).toList(),
        projetoEncontrado.getColaboradores().stream().map(Colaborador::getIdColaborador).toList()
    );
    }

    //criar projeto
    // FUNCIONANDO
    @PostMapping("/criar")
    public ProjetoDTO criarProjeto(@RequestBody ProjetoDTO dto, Authentication authentication) {
        UUID idManager = UUID.fromString(authentication.getName());
        // Executa o seu service normalmente
        Projeto projeto = projetoService.criarProjeto(dto, idManager);

        return new ProjetoDTO(
            projeto.getId(),
            projeto.getNome(),
            projeto.getDescricao(),
            projeto.getDataInicio(),
            projeto.getDataEntrega(),
            projeto.getOrcamento(),
            projeto.getIdManager(),
            List.of(),
            List.of()
        );
    }

    //associar projeto a cliente
    // FUNCIONANDO
    @PostMapping("/{idProjeto}/cliente/{idCliente}")
    public void associarProjetoACliente(@PathVariable UUID idProjeto, @PathVariable UUID idCliente, Authentication authentication) {
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        projetoService.associarProjetoACliente(idProjeto, idCliente, idManagerLogado);
    }

    //deletar associação projeto-cliente
    // FUNCIONANDO
    @DeleteMapping("/{idProjeto}/cliente/{idCliente}")
    public void deletarClienteDeProjeto(@PathVariable UUID idProjeto, @PathVariable UUID idCliente, Authentication authentication) {
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        projetoService.deletarClienteDeProjeto(idProjeto, idCliente, idManagerLogado);
    }

    //listar clientes de um projeto
    // FUNCIONANDO
    @GetMapping("/{idProjeto}/clientes")
    public List<Cliente> listarClientesDeProjeto(@PathVariable UUID idProjeto, Authentication authentication) {
    UUID idManagerLogado = UUID.fromString(authentication.getName());
    // Retorna direto
    return projetoService.listarClientesDeProjeto(idProjeto, idManagerLogado);  
    }

    //associar projeto a colaborador
    // FUNCIONANDO
    @PostMapping("/{idProjeto}/colaborador/{idColaborador}")
    public void associarProjetoAColaborador(@PathVariable UUID idProjeto, @PathVariable UUID idColaborador, Authentication authentication) {
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        projetoService.associarColaboradorAProjeto(idProjeto, idColaborador, idManagerLogado);
    }

    //deletar associação projeto-colaborador
    // FUNCIONANDO
    @DeleteMapping("/{idProjeto}/colaborador/{idColaborador}")
    public void deletarColaboradorDeProjeto(@PathVariable UUID idProjeto, @PathVariable UUID idColaborador, Authentication authentication) {
        UUID idManagerLogado = UUID.fromString(authentication.getName());
        projetoService.deletarColaboradorDeProjeto(idProjeto, idColaborador, idManagerLogado);
    }

    //listar colaboradores de um projeto
    // FUNCIONANDO
    @GetMapping("/{idProjeto}/colaboradores")
    public List<Colaborador> listarColaboradoresDeProjeto(@PathVariable UUID idProjeto, Authentication authentication) { 
    UUID idManagerLogado = UUID.fromString(authentication.getName());
    // Retorna direto
    return projetoService.listarColaboradoresDeProjeto(idProjeto, idManagerLogado);  
    }

    // listar TODOS OS CLIENTES
    @GetMapping("/clientes")
    public List<Cliente> listarClientes() {
        return projetoService.listarTodosClientes();
    }

    // listar TODOS OS COLABORADORES
    @GetMapping("/colaboradores")
    public List<Colaborador> listarColaboradores() {
        return projetoService.listarTodosColaboradores();
    }
    
    
}
