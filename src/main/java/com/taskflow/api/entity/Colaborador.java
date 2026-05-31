package com.taskflow.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "colaborador", schema = "projetos")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_colaborador")
    private UUID idColaborador;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;


    // tabela intermediária: relação N:N entre colaborador e especialidade
    @ManyToMany
    @JoinTable(
            name="colaborador_especialidade",
            schema = "projetos",
            joinColumns = @JoinColumn(name = "id_colaborador"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidade")
    )
    private List<Especialidade> especialidades;

    // tabela intermediária: relação N:N entre colaborador e atividade
    @ManyToMany
    @JoinTable(
            name = "colaborador_atividade",
            schema = "projetos",
            joinColumns = @JoinColumn(name = "id_colaborador"),
            inverseJoinColumns = @JoinColumn(name = "id_atividade")
    )
    private List<Atividade> atividades;

    // tabela intermediária: relação N:N entre colaborador e tarefa
    @ManyToMany
    @JoinTable(
            name = "colaborador_tarefa",
            schema = "projetos",
            joinColumns = @JoinColumn(name = "id_colaborador"),
            inverseJoinColumns = @JoinColumn(name = "id_tarefa")
    )
    private List<Tarefa> tarefas;
}
