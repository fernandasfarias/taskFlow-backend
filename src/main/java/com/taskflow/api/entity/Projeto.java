package com.taskflow.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "projeto", schema = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_projeto")
    private UUID idProjeto;

    @Column(name = "nome_projeto", nullable = false)
    private String nomeProjeto;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;

    @Column(nullable = false)
    private Float orcamento;

    // relação N:1 entre Projeto e Project Manager
    @ManyToOne
    @JoinColumn(name = "id_manager", nullable = false)
    private ProjectManager projectManager;

    // tabela intermediária: Relação N:N entre Projeto e Cliente
    @ManyToMany
    @JoinTable(
            name = "projeto_cliente",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente")
    )
    private List<Cliente> clientes;

    // tabela intermediária: Relação N:N entre Projeto e Colaborador
    @ManyToMany
    @JoinTable(
            name = "projeto_colaborador",
            joinColumns = @JoinColumn(name = "id_projeto"),
            inverseJoinColumns = @JoinColumn(name = "id_colaborador")
    )
    private List<Colaborador> colaboradores;

    // para acessar todas as atividades de um projeto
    @OneToMany(mappedBy = "projeto")
    private List<Atividade> atividades;

    // para acessar todas as mensagens de um projeto
    @OneToMany(mappedBy = "projeto")
    private List<Mensagem> mensagens;

}
