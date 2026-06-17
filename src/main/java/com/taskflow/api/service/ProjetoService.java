package com.taskflow.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.taskflow.api.dto.ProjetoDTO;
import com.taskflow.api.repository.ProjectManagerRepository;
import com.taskflow.api.repository.ProjetoRepository;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.repository.ClienteRepository;
import com.taskflow.api.entity.Cliente;
import com.taskflow.api.repository.ColaboradorRepository;
import com.taskflow.api.entity.Colaborador;

import java.util.List;
import java.util.Collections;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final ClienteRepository clienteRepository;
    private final ColaboradorRepository colaboradorRepository;



    //atualizar projeto
    public void editarProjeto(UUID idProjeto, ProjetoDTO dto, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        // 2. VALIDAÇÃO:O gerente logado é o dono desse projeto?
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado: Você não é o gerente deste projeto.");
        }

        //atualiza somente os campos que foram preenchidos
        if (dto.nome() != null) {
            projeto.setNome(dto.nome());
        }

        if (dto.descricao() != null) {
            projeto.setDescricao(dto.descricao());
        }

        if (dto.dataInicio() != null) {
            projeto.setDataInicio(dto.dataInicio());
        }

        if (dto.dataEntrega() != null) {
            projeto.setDataEntrega(dto.dataEntrega());
        }

        if (dto.orcamento() != null) {
            projeto.setOrcamento(dto.orcamento());
        }

        projetoRepository.save(projeto);
    }
    
    //deletar projeto
    @Transactional
        public void deletarProjeto(UUID idProjeto, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        // 2. VALIDAÇÃO: O gerente logado é o dono desse projeto?
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }

        // 3. Se passou, deleta
        projetoRepository.delete(projeto);
    }

    public List<Projeto> listarProjetos(UUID idManagerLogado) {
    // Busca todos os projetos onde o ID do gerente seja igual ao logado
    return projetoRepository.findByProjectManagerIdManager(idManagerLogado);
    }

   public Projeto mostrarProjetoPorNome(String nome) {
    return projetoRepository.findByNomeProjeto(nome) 
        .orElseThrow(() -> new RuntimeException("Projeto não encontrado com o nome: " + nome));
    }

    public Projeto mostrarProjetoPorId(UUID id) {
    return projetoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Projeto não encontrado com o ID: "
            + id));
    }


    //criar projeto
    public void criarProjeto(ProjetoDTO dto, UUID idManager) {
     var projeto = new Projeto();
     projeto.setNome(dto.nome());
     projeto.setDescricao(dto.descricao());
     projeto.setDataInicio(dto.dataInicio());
     projeto.setDataEntrega(dto.dataEntrega());
     projeto.setOrcamento(dto.orcamento());
    
     // idManager que veio do controller
     ProjectManager manager = projectManagerRepository.getReferenceById(idManager);
    projeto.setProjectManager(manager);
    
    projetoRepository.save(projeto);
    }

    //associar projeto a cliente
    public void associarProjetoACliente(UUID idProjeto, UUID idCliente, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }

        // 2. Busca o cliente no banco
        Cliente cliente = clienteRepository.findByidCliente(idCliente)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (projeto.getClientes().contains(cliente)) {
        throw new RuntimeException("Este cliente já está associado a este projeto.");
        }
        // 3. Associa o cliente ao projeto
        projeto.getClientes().add(cliente);
        projetoRepository.save(projeto);
    }        

    //deletar cliente de um projeto
    public void deletarClienteDeProjeto(UUID idProjeto, UUID idCliente, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto) 
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado")); 
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }
        //busca o cliente no banco
        Cliente cliente = clienteRepository.findByidCliente(idCliente)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (!projeto.getClientes().contains(cliente)) {
        throw new RuntimeException("Este cliente não está associado a este projeto.");
        }
        projeto.getClientes().remove(cliente);
        projetoRepository.save(projeto);
    }

    //listar clientes de um projeto
    public List<Cliente> listarClientesDeProjeto(UUID idProjeto, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }
        return projeto.getClientes();
    }

    //associar colaborador a projeto
    public void associarColaboradorAProjeto(UUID idProjeto, UUID idColaborador, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }

        // busca o colaborador no banco
        Colaborador colaborador = colaboradorRepository.findByIdColaborador(idColaborador)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        if (projeto.getColaboradores().contains(colaborador)) {
        throw new RuntimeException("Este colaborador já está associado a este projeto.");  
        }
        
        projeto.getColaboradores().add(colaborador);
        projetoRepository.save(projeto);
        }

        //deletar colaborador de um projeto
        public void deletarColaboradorDeProjeto(UUID idProjeto, UUID idColaborador, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto) 
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado")); 
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }
        //busca o colaborador no banco
        Colaborador colaborador = colaboradorRepository.findByIdColaborador(idColaborador)
            .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        if (!projeto.getColaboradores().contains(colaborador)) {
        throw new RuntimeException("Este colaborador não está associado a este projeto.");
        }
        projeto.getColaboradores().remove(colaborador);
        projetoRepository.save(projeto);
    }

     //listar colaboradores de um projeto
     public List<Colaborador> listarColaboradoresDeProjeto(UUID idProjeto, UUID idManagerLogado) {
        // 1. Busca o projeto no banco
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        if (!projeto.getIdManager().equals(idManagerLogado)) {
            throw new RuntimeException("Acesso negado");
        }
        return projeto.getColaboradores();
     }

}
        



