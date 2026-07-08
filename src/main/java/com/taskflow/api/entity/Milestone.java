package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "milestone", schema = "projetos")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_milestone")
    private UUID idMilestone;

    @Column(name = "nome_milestone", nullable = false)
    private String nomeMilestone;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Etapa> etapas = new ArrayList<>();
}