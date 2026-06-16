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

        public UUID getId() {
        return idProjeto;
    }
    public String getNome() {
        return nomeProjeto;
    }
    public String getDescricao() {
        return descricao;   
    }
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    public LocalDate getDataEntrega() {
        return dataEntrega;
    }
    public Float getOrcamento() {
        return orcamento;
    }
    public UUID getIdManager() {
        return projectManager.getIdManager();
    }

    public List<Cliente> getClientes() {
    return this.clientes;
    }
    
    public List<Colaborador> getColaboradores() {
    return this.colaboradores;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setColaboradores(List<Colaborador> colaboradores) {
        this.colaboradores = colaboradores;
    }
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public void setId(UUID idProjeto) {
        this.idProjeto = idProjeto;
    }
    public void setNome(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;                   
    }
    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    public void setOrcamento(Float orcamento) {
        this.orcamento = orcamento;
    }

}
