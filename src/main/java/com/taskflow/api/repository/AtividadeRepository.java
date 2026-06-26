package com.taskflow.api.repository;

import com.taskflow.api.entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {

    List<Atividade> findByProjetoIdProjeto(UUID idProjeto);

    List<Atividade> findByMilestoneIdMilestone(UUID idMilestone);
}