package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "especialidade", schema = "projetos")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_especialidade")
    private UUID idEspecialidade;

    @Column(name = "nome_especialidade", nullable = false)
    private String nomeEspecialidade;
}
