package com.taskflow.api.repository;

import com.taskflow.api.entity.Colaborador;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface ColaboradorRepository
        extends JpaRepository<Colaborador, UUID>{
            boolean existsByEmail(String email);
            Optional<Colaborador> findByEmail(String email);
}
