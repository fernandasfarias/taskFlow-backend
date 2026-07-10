package com.taskflow.api.repository;

import com.taskflow.api.entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface AtividadeRepository extends JpaRepository<Atividade, UUID> {

        List<Atividade> findByProjetoIdProjeto(UUID idProjeto);
        List<Atividade> findByMilestoneIdMilestone(UUID idMilestone);


        @Modifying
        @Transactional
        @Query(
                value = """
                INSERT INTO projetos.colaborador_atividade 
                (id_colaborador, id_atividade)
                VALUES (:idColaborador, :idAtividade)
                """, nativeQuery = true )
                void associar(
                        @Param("idColaborador") UUID idColaborador,
                        @Param("idAtividade") UUID idAtividade
                );
        
        @Modifying
        @Query("""
                DELETE FROM Atividade a
                WHERE a.projeto.idProjeto = :idProjeto
        """)
        void deleteByProjeto(@Param("idProjeto") UUID idProjeto);

        @Modifying
        @Query("""
        UPDATE Atividade a
        SET a.milestone = null
        WHERE a.projeto.idProjeto = :idProjeto
        """)
        void desvincularMilestones(@Param("idProjeto") UUID idProjeto);
}