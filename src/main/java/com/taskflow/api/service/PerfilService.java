package com.taskflow.api.service;

import com.taskflow.api.dto.AtualizarPerfilDTO;
import com.taskflow.api.dto.AuthResponseDTO;
import com.taskflow.api.dto.PerfilDTO;
import com.taskflow.api.dto.UsuarioAutenticadoDTO;
import com.taskflow.api.entity.Cliente;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final JwtService jwtService;
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

    public PerfilDTO buscarPerfil(UUID id, String role){
        if(role.equals("ROLE_PROJECT_MANAGER")){
            ProjectManager pm = projectManagerRepository.findById(id).orElseThrow(() -> new RuntimeException("Project Manager não encontrado."));
            return new PerfilDTO(pm.getNomeManager(), pm.getEmail(), "******");
        }
        if(role.equals("ROLE_CLIENTE")){
            Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            return new PerfilDTO(cliente.getNomeCliente(), cliente.getEmail(), "******");
        }
        if(role.equals("ROLE_COLABORADOR")){
            Colaborador col = colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
            return new PerfilDTO(col.getNome(), col.getEmail(), "******");
        }
        throw new RuntimeException("Tipo de usuário inválido");
    }
}
