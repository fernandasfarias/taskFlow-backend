package com.taskflow.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

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

    public Empresa() {
    }

    public UUID getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(UUID idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
