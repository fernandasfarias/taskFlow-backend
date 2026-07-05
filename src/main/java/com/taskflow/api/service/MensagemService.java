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
import com.taskflow.api.entity.Cliente;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    public Mensagem enviarMensagem(MensagemRequestDTO mensagemRequestDTO, String idUsuarioLogado, TipoUsuario role){

        // buscar o projeto
        Projeto projeto = projetoRepository.findById(mensagemRequestDTO.idProjeto())
            .orElseThrow(()-> new RuntimeException("Projeto não encontrado"));

        // validar permissão agora usando ID em vez de email
        boolean temAcesso = false;

        if(role == TipoUsuario.PROJECT_MANAGER){
            if(projeto.getProjectManager().getIdManager().toString().equals(idUsuarioLogado)){
                temAcesso = true;
            }
        }else if(role == TipoUsuario.CLIENTE){
            temAcesso = projeto.getClientes().stream()
                .anyMatch(c -> c.getIdCliente().toString().equals(idUsuarioLogado));
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
                .filter(c -> c.getIdCliente().toString().equals(idUsuarioLogado))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado no projeto"));
            mensagem.setCliente(clienteRemetente);
        }

        return mensagemRepository.save(mensagem);
    }
    
    public List<Mensagem> listarMensagensPorProjeto(UUID idProjeto, String idUsuarioLogado, TipoUsuario role){
        Projeto projeto = projetoRepository.findById(idProjeto)
            .orElseThrow(()-> new RuntimeException("Projeto não encontrado"));

        // validar permissão idêntica a do envio (agora com ID)
        boolean temAcesso = false;

        if(role == TipoUsuario.PROJECT_MANAGER){
            if(projeto.getProjectManager().getIdManager().toString().equals(idUsuarioLogado)){
                temAcesso = true;
            }
        }else if(role == TipoUsuario.CLIENTE){
            temAcesso = projeto.getClientes().stream()
                .anyMatch(c -> c.getIdCliente().toString().equals(idUsuarioLogado));
        }

        if(!temAcesso){
            throw new RuntimeException("Usuário não tem permissão para visualizar mensagens deste projeto");
        }

        return mensagemRepository.findByProjetoOrderByDataHoraAsc(projeto);
    }
}