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

    @Modifying
    @Query("""
        DELETE FROM Tarefa t
        WHERE t.atividade.projeto.idProjeto = :idProjeto
    """)
    void deleteByProjeto(@Param("idProjeto") UUID idProjeto);

    @Modifying
    @Query(value = """
        DELETE FROM projetos.colaborador_tarefa
        WHERE id_tarefa IN (
            SELECT id_tarefa
            FROM projetos.tarefa
            WHERE id_atividade IN (
                SELECT id_atividade
                FROM projetos.atividade
                WHERE id_projeto = :idProjeto
            )
        )
        """, nativeQuery = true)
    void deleteColaboradorTarefas(@Param("idProjeto") UUID idProjeto);

    @Modifying
    @Query("""
        DELETE FROM Tarefa t
        WHERE t.atividade.idAtividade = :idAtividade
    """)
    void deleteByAtividade(@Param("idAtividade") UUID idAtividade);

        @Modifying
        @Query(value = """
            DELETE FROM projetos.colaborador_tarefa
            WHERE id_tarefa IN (
                SELECT id_tarefa
                FROM projetos.tarefa
                WHERE id_atividade = :idAtividade
            )
        """, nativeQuery = true)
        void deleteColaboradorTarefasByAtividade(
            @Param("idAtividade") UUID idAtividade
    );

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM projetos.colaborador_tarefa
        WHERE id_tarefa = :idTarefa
        """, nativeQuery = true)
    void deleteColaboradorTarefa(@Param("idTarefa") UUID idTarefa);
}