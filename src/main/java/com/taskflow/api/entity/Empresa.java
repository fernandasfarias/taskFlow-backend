package com.taskflow.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "empresa", schema = "projetos")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_empresa")
    private UUID idEmpresa;

    @Column(name = "nome_empresa", nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false, unique = true)
    private String cnpj;
}
