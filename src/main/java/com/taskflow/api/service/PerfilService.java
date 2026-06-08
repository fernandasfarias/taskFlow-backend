package com.taskflow.api.service;

import com.taskflow.api.dto.AtualizarPerfilDTO;
import com.taskflow.api.entity.Cliente;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final ProjectManagerRepository projectManagerRepository;
    private final ClienteRepository clienteRepository;
    private final ColaboradorRepository colaboradorRepository;

    // atualizar dados do perfil
    public void atualizar(String email, AtualizarPerfilDTO dto) {

        System.out.println("EMAIL RECEBIDO: " + email);
        if (dto.getNome() == null && dto.getEmail() == null && dto.getSenha() == null) {
            throw new RuntimeException("Nada para atualizar");
        }

        if (projectManagerRepository.findByEmail(email).isPresent()) {
            var pm = projectManagerRepository.findByEmail(email).get();

            if (dto.getNome() != null) pm.setNomeManager(dto.getNome());
            if (dto.getEmail() != null) pm.setEmail(dto.getEmail());
            if (dto.getSenha() != null) pm.setSenha(dto.getSenha());

            projectManagerRepository.save(pm);
            return;
        }

        if (clienteRepository.findByEmail(email).isPresent()) {
            var cliente = clienteRepository.findByEmail(email).get();

            if (dto.getNome() != null) cliente.setNomeCliente(dto.getNome());
            if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
            if (dto.getSenha() != null) cliente.setSenha(dto.getSenha());

            clienteRepository.save(cliente);
            return;
        }

        if (colaboradorRepository.findByEmail(email).isPresent()) {
            var colaborador = colaboradorRepository.findByEmail(email).get();

            if (dto.getNome() != null) colaborador.setNome(dto.getNome());
            if (dto.getEmail() != null) colaborador.setEmail(dto.getEmail());
            if (dto.getSenha() != null) colaborador.setSenha(dto.getSenha());

            colaboradorRepository.save(colaborador);
            return;
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void removerPerfil(String email){
        var pm = projectManagerRepository.findByEmail(email);
        if(pm.isPresent()){
            projectManagerRepository.delete(pm.get());
            return;
        }
        var cliente = clienteRepository.findByEmail(email);
        if(cliente.isPresent()){
            clienteRepository.delete(cliente.get());
            return;
        }
        var colaborador = colaboradorRepository.findByEmail(email);
        if(colaborador.isPresent()){
            colaboradorRepository.delete(colaborador.get());
            return;
        }
        throw new RuntimeException("Usuário não encontrado");
    }
}
