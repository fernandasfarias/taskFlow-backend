package com.taskflow.api.repository;

import com.taskflow.api.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpresaRepository
        extends JpaRepository<Empresa, UUID> {
    Optional<Empresa> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);
}
