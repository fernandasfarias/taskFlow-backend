package com.taskflow.api.service;

import com.taskflow.api.config.PasswordConfig;
import com.taskflow.api.dto.projectManagerDTO.ProjectManagerResponseDTO;
import org.springframework.stereotype.Service;

import com.taskflow.api.dto.projectManagerDTO.CadastroProjectManagerDTO;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectManagerService {
    private final ProjectManagerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ProjectManagerResponseDTO cadastrar(CadastroProjectManagerDTO dto){

        // verificando se já existe esse email no banco de dados
        if(repository.existsByEmail(dto.email())){
            throw new RuntimeException("Email já cadastrado");
        }

        ProjectManager manager = new ProjectManager();

        manager.setNomeManager(dto.nome());
        manager.setEmail(dto.email());
        manager.setSenha(passwordEncoder.encode(dto.senha()));

        ProjectManager saved = repository.save(manager);

        return new ProjectManagerResponseDTO(
                saved.getIdManager(),
                saved.getNomeManager(),
                saved.getEmail()
        );

    }
}
