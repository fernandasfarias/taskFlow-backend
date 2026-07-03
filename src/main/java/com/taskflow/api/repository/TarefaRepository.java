package com.taskflow.api.repository;

import com.taskflow.api.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {

    List<Tarefa> findByAtividade_IdAtividade(UUID idAtividade);

}