package com.taskflow.api.repository;

import com.taskflow.api.entity.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EtapaRepository extends JpaRepository<Etapa, UUID> {
}