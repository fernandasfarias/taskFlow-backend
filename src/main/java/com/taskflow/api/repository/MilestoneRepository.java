package com.taskflow.api.repository;

import com.taskflow.api.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    
    @Query("SELECT m FROM Milestone m JOIN Atividade a ON a.milestone.idMilestone = m.idMilestone WHERE a.idAtividade = :idAtividade")
    List<Milestone> findByAtividade_IdAtividade(@Param("idAtividade") UUID idAtividade);
    
    @Modifying
    @Query("""
        DELETE FROM Milestone m
        WHERE m.idMilestone IN (
            SELECT a.milestone.idMilestone
            FROM Atividade a
            WHERE a.projeto.idProjeto = :idProjeto
            AND a.milestone IS NOT NULL
        )
    """)
    void deleteByProjeto(@Param("idProjeto") UUID idProjeto);
}