package com.taskflow.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false)
    private String tokenRecuperacaoSenha;

    @Column(nullable = false)
    private LocalDateTime expiracaoTokenRecuperacaoSenha;

    public UUID getIdManager() {
        return idManager;
    }

    public void setIdManager(UUID idManager) {
        this.idManager = idManager;
    }

    public String getNomeManager() {
        return nomeManager;
    }

    public void setNomeManager(String nomeManager) {
        this.nomeManager = nomeManager;
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

    public List<Certificacao> getCertificacoes() {
        return certificacoes;
    }

    public void setCertificacoes(List<Certificacao> certificacoes) {
        this.certificacoes = certificacoes;
    }

    public String getTokenRecuperacaoSenha() { return tokenRecuperacaoSenha; }

    public void setTokenRecuperacaoSenha(String tokenRecuperacaoSenha) { this.tokenRecuperacaoSenha = tokenRecuperacaoSenha; }

    public LocalDateTime getExpiracaoTokenRecuperacaoSenha() { return expiracaoTokenRecuperacaoSenha; }

    public void setExpiracaoTokenRecuperacaoSenha(LocalDateTime expiracaoTokenRecuperacaoSenha) { this.expiracaoTokenRecuperacaoSenha = expiracaoTokenRecuperacaoSenha; }

    public ProjectManager(){}

    // para acessar todos os certificados do Project Manager
    @OneToMany(mappedBy = "projectManager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificacao>certificacoes = new ArrayList<>();
}
