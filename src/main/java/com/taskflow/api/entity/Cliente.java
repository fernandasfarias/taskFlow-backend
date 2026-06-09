package com.taskflow.api.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente", schema = "projetos")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cliente")
    private UUID idCliente;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String tokenRecuperacaoSenha;

    @Column(nullable = false)
    private LocalDateTime expiracaoTokenRecuperacaoSenha;

    public Cliente(){}

    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getTokenRecuperacaoSenha() { return tokenRecuperacaoSenha; }

    public void setTokenRecuperacaoSenha(String tokenRecuperacaoSenha) { this.tokenRecuperacaoSenha = tokenRecuperacaoSenha; }

    public LocalDateTime getExpiracaoTokenRecuperacaoSenha() { return expiracaoTokenRecuperacaoSenha; }

    public void setExpiracaoTokenRecuperacaoSenha(LocalDateTime expiracaoTokenRecuperacaoSenha) { this.expiracaoTokenRecuperacaoSenha = expiracaoTokenRecuperacaoSenha; }


    // Relação N:1 entre Cliente e Empresa
    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = true)
    private Empresa empresa;
}
