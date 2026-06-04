package com.taskflow.api.entity;

import com.taskflow.api.enums.Status;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "atividade", schema="projetos")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_atividade")
    private UUID idAtividade;

    @Column(name = "nome_atividade", nullable = false)
    private String nomeAtividade;

    @Column(name = "descricao_atividade", nullable = false)
    private String descricaoAtividade;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atividade", nullable = false)
    private Status statusAtividade;

    // relação N:1 entre Atividade e Projeto
    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Projeto projeto;

    // relação N:1 entre Atividade e Milestone
    @ManyToOne
    @JoinColumn(name = "id_milestone", nullable = false)
    private Milestone milestone;
}
