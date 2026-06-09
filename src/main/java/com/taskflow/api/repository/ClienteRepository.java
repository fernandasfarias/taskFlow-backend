package com.taskflow.api.repository;

import com.taskflow.api.entity.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface ClienteRepository
        extends JpaRepository<Cliente, UUID>{
            boolean existsByEmail(String email);
            Optional<Cliente> findByEmail(String email);

            Optional<Cliente> findByTokenRecuperacaoSenha(String token);
}
