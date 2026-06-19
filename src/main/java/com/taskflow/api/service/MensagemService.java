package com.taskflow.api.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.taskflow.api.dto.MensagemRequestDTO;
import com.taskflow.api.entity.Mensagem;
import com.taskflow.api.repository.MensagemRepository;
import com.taskflow.api.repository.ProjetoRepository;
import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.entity.Projeto;
import com.taskflow.api.service.JwtService;
import com.taskflow.api.entity.Cliente;


@Service
@RequiredArgsConstructor

public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ProjetoRepository projetoRepository;
    private final JwtService jwtService;

    @Transactional
    public Mensagem enviarMensagem(MensagemRequestDTO mensagemRequestDTO, String emailUsuariologado, TipoUsuario role){

        //buscar o projeto
        Projeto projeto = projetoRepository.findById(mensagemRequestDTO.idProjeto()).orElseThrow(()-> new RuntimeException("Projeto não encontrado"));

        //validar permição
        boolean temAcesso = false;

        if(role == TipoUsuario.PROJECT_MANAGER){
            if(projeto.getProjectManager().getEmail().equals(emailUsuariologado)){ //verifica se o email logado é o mesmo do project manager do projeto
                temAcesso = true;
            }
        }else if(role == TipoUsuario.CLIENTE){
            temAcesso = projeto.getClientes().stream().anyMatch(c -> emailUsuariologado.equals(c.getEmail())); //verifica se o email logado é o mesmo de algum cliente do projeto   
        }

        if(!temAcesso){
            throw new RuntimeException("Usuário não tem permissão para enviar mensagem para este projeto");
        }

        // persistência da mensagem
        Mensagem mensagem = new Mensagem();
        mensagem.setConteudo(mensagemRequestDTO.conteudo());
        mensagem.setProjeto(projeto);

        if (role == TipoUsuario.PROJECT_MANAGER) {
         mensagem.setProjectManager(projeto.getProjectManager());
        } else if (role == TipoUsuario.CLIENTE) {
        Cliente clienteRemetente = projeto.getClientes().stream()
            .filter(c -> emailUsuariologado.equals(c.getEmail()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado no projeto"));
        mensagem.setCliente(clienteRemetente);
}

        return mensagemRepository.save(mensagem);
    }
    
}
