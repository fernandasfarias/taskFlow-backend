package com.taskflow.api.repository;

import com.taskflow.api.entity.Mensagem;
import com.taskflow.api.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {
    List<Mensagem> findByProjetoOrderByDataHoraAsc(Projeto projeto);
}