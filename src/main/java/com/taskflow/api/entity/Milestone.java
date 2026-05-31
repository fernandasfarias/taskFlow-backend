package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "milestone", schema = "projetos")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_milestone")
    private UUID idMilestone;

    @Column(name = "nome_milestone", nullable = false)
    private String nomeMilestone;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;
}
