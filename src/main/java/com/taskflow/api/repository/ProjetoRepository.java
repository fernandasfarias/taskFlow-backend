package com.taskflow.api.repository;

import com.taskflow.api.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

        @Query(value = """
        SELECT DISTINCT p.*
        FROM projetos.projeto p
        LEFT JOIN projetos.projeto_cliente pc 
           ON pc.id_projeto = p.id_projeto
        LEFT JOIN projetos.projeto_colaborador pc2 
           ON pc2.id_projeto = p.id_projeto
        WHERE p.id_manager = :idUsuario
           OR pc.id_cliente = :idUsuario
           OR pc2.id_colaborador = :idUsuario
    """, nativeQuery = true)
        List<Projeto> buscarProjetosDoUsuario(@Param("idUsuario") UUID idUsuario);

        @Query(value = """
    SELECT DISTINCT p.*
    FROM projetos.projeto p
    LEFT JOIN projetos.projeto_cliente pc 
           ON pc.id_projeto = p.id_projeto
    LEFT JOIN projetos.projeto_colaborador pc2 
           ON pc2.id_projeto = p.id_projeto
    WHERE (
        p.id_manager = :idUsuario
        OR pc.id_cliente = :idUsuario
        OR pc2.id_colaborador = :idUsuario
    )
    AND (
        LOWER(p.nome_projeto) LIKE LOWER(CONCAT('%', :termo, '%'))
        OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%'))
    )
    """, nativeQuery = true)
        List<Projeto> buscarProjetosDoUsuarioPorTermo(
                @Param("idUsuario") UUID idUsuario,
                @Param("termo") String termo
        );

        @Modifying
        @Query(value = """
            DELETE FROM projetos.projeto_colaborador
            WHERE id_projeto = :idProjeto
            """, nativeQuery = true)
        void deleteProjetoColaboradores(@Param("idProjeto") UUID idProjeto);

        @Modifying
        @Query(value = """
            DELETE FROM projetos.projeto_cliente
            WHERE id_projeto = :idProjeto
            """, nativeQuery = true)
        void deleteProjetoClientes(@Param("idProjeto") UUID idProjeto);

        @Modifying
        @Query(value = """
            DELETE FROM projetos.colaborador_atividade
            WHERE id_atividade IN (
                SELECT id_atividade
                FROM projetos.atividade
                WHERE id_projeto = :idProjeto
            )
            """, nativeQuery = true)
        void deleteColaboradorAtividades(@Param("idProjeto") UUID idProjeto);
}

