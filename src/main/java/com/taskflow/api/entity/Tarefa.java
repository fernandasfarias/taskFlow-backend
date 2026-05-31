package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tarefa", schema = "projetos")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tarefa")
    private UUID idTarefa;

    @Column(name = "nome_tarefa", nullable = false)
    private String nomeTarefa;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_tarefa", nullable = false)
    private Status statusTarefa;

    // relação N:1 entre Tarefa e Atividade
    @ManyToOne
    @JoinColumn(name = "id_atividade", nullable = false)
    private Atividade atividade;
}
