package com.taskflow.api.repository;

import com.taskflow.api.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

import java.util.UUID;

@Repository
public interface ProjetoRepository
        extends JpaRepository<Projeto, UUID>{
        Optional<Projeto> findByNomeProjeto (String nomeProjeto);
        List<Projeto> findByProjectManagerIdManager(UUID idManager);
        Optional<Projeto> findById(UUID id);

}

