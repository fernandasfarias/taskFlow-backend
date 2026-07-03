package com.taskflow.api.repository;

import com.taskflow.api.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {

    List<Tarefa> findByAtividade_IdAtividade(UUID idAtividade);

    @Modifying
    @Transactional
    @Query(
            value = """
            INSERT INTO projetos.colaborador_tarefa 
            (id_colaborador, id_tarefa)
            VALUES (:idColaborador, :idTarefa)
        """, nativeQuery = true )
    void associar(
            @Param("idColaborador") UUID idColaborador,
            @Param("idTarefa") UUID idTarefa
    );

}