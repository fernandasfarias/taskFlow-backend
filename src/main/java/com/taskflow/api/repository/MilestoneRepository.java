package com.taskflow.api.repository;

import com.taskflow.api.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    
    @Query("SELECT m FROM Milestone m JOIN Atividade a ON a.milestone.idMilestone = m.idMilestone WHERE a.idAtividade = :idAtividade")
    List<Milestone> findByAtividade_IdAtividade(@Param("idAtividade") UUID idAtividade);
}