package com.taskflow.api.repository;

import com.taskflow.api.entity.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EtapaRepository extends JpaRepository<Etapa, UUID> {
    @Modifying
    @Query("""
        DELETE FROM Etapa e
        WHERE e.milestone.idMilestone IN (
            SELECT a.milestone.idMilestone
            FROM Atividade a
            WHERE a.projeto.idProjeto = :idProjeto
              AND a.milestone IS NOT NULL
        )
    """)
    void deleteByProjeto(@Param("idProjeto") UUID idProjeto);
}