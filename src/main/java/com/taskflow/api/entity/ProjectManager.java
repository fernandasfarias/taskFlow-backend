package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "project_manager", schema = "projetos")
public class ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_manager")
    private UUID idManager;

    @Column(name = "nome_manager", nullable = false)
    private String nomeManager;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    // para acessar todos os certificados do Project Manager
    @OneToMany(mappedBy = "projectManager")
    private List<Certificacao>certificacoes;
}
