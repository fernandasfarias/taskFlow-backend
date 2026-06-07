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
    public void atualizar(UUID id, AtualizarPerfilDTO dto) {
        if (dto.getNome() == null && dto.getEmail() == null && dto.getSenha() == null) {
            throw new RuntimeException("Nada para atualizar");
        }

        if (projectManagerRepository.findById(id).isPresent()) {
            var pm = projectManagerRepository.findById(id).get();

            if (dto.getNome() != null) pm.setNomeManager(dto.getNome());
            if (dto.getEmail() != null) pm.setEmail(dto.getEmail());
            if (dto.getSenha() != null) pm.setSenha(dto.getSenha());

            projectManagerRepository.save(pm);
            return;
        }

        if (clienteRepository.findById(id).isPresent()) {
            var cliente = clienteRepository.findById(id).get();

            if (dto.getNome() != null) cliente.setNomeCliente(dto.getNome());
            if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
            if (dto.getSenha() != null) cliente.setSenha(dto.getSenha());

            clienteRepository.save(cliente);
            return;
        }

        if (colaboradorRepository.findById(id).isPresent()) {
            var colaborador = colaboradorRepository.findById(id).get();

            if (dto.getNome() != null) colaborador.setNome(dto.getNome());
            if (dto.getEmail() != null) colaborador.setEmail(dto.getEmail());
            if (dto.getSenha() != null) colaborador.setSenha(dto.getSenha());

            colaboradorRepository.save(colaborador);
            return;
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void removerPerfil(UUID id){
        if(projectManagerRepository.existsById(id)){
            projectManagerRepository.deleteById(id);
            return;
        }
        if(clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            return;
        }
        if(colaboradorRepository.existsById(id)){
            colaboradorRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("Usuário não encontrado");
    }
}
