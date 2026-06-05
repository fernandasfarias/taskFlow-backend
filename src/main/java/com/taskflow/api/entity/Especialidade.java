package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "especialidade", schema = "projetos")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_especialidade")
    private UUID idEspecialidade;

    @Column(name = "nome_especialidade", nullable = false, unique = true)
    private String nomeEspecialidade;

    public Especialidade() {
    }

    public UUID getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(UUID idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Especialidade)) return false;
        Especialidade that = (Especialidade) o;
        return idEspecialidade != null &&
                idEspecialidade.equals(that.idEspecialidade);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
