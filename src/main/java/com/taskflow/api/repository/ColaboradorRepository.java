package com.taskflow.api.repository;

import com.taskflow.api.entity.Colaborador;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

@Repository
public interface ColaboradorRepository
        extends JpaRepository<Colaborador, UUID>{
            boolean existsByEmail(String email);
            Optional<Colaborador> findByEmail(String email);
            Optional<Colaborador> findByIdColaborador(UUID idColaborador);

            Optional<Colaborador> findByTokenRecuperacaoSenha(String token);
}
