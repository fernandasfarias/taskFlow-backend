package com.taskflow.api.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

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

    @Column(nullable = false)
    private String tokenRecuperacaoSenha;

    @Column(nullable = false)
    private LocalDateTime expiracaoTokenRecuperacaoSenha;

    public UUID getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(UUID idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Especialidade> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
    }

    public String getTokenRecuperacaoSenha() { return tokenRecuperacaoSenha; }

    public void setTokenRecuperacaoSenha(String tokenRecuperacaoSenha) { this.tokenRecuperacaoSenha = tokenRecuperacaoSenha; }

    public LocalDateTime getExpiracaoTokenRecuperacaoSenha() { return expiracaoTokenRecuperacaoSenha; }

    public void setExpiracaoTokenRecuperacaoSenha(LocalDateTime expiracaoTokenRecuperacaoSenha) { this.expiracaoTokenRecuperacaoSenha = expiracaoTokenRecuperacaoSenha; }

    public Colaborador(){}

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
