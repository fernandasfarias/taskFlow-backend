package com.taskflow.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "etapa_milestone", schema = "projetos")
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_etapa")
    private UUID idEtapa;

    @Column(name = "nome_etapa", nullable = false)
    private String nomeEtapa;

    @Column(name = "concluida", nullable = false)
    private Boolean concluida = false;

    @ManyToOne
    @JoinColumn(name = "id_milestone", nullable = false)
    @JsonIgnore
    private Milestone milestone;
}