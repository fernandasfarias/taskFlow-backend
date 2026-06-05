package com.taskflow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taskflow.api.entity.Especialidade;

import javax.swing.text.html.Option;
import java.util.UUID;

import java.util.Optional;

@Repository
public interface EspecialidadeRepository
    extends JpaRepository<Especialidade, UUID>{
    Optional<Especialidade> findByNomeEspecialidade(String NomeEspecialidade);
}
