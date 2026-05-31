package com.taskflow.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mensagem", schema = "projetos")
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mensagem")
    private UUID idMensagem;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    // metodo para preenchimento automático da data
    @PrePersist
    public void preencherDataHoraEnvio(){
        this.dataHora = LocalDateTime.now();
    }

    // relação N:1 entre Mensagem e Projeto
    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Projeto projeto;

    // relação N:1 entre Mensagem e Project Manager
    @ManyToOne
    @JoinColumn(name = "id_manager", nullable = false)
    private ProjectManager projectManager;

    // relação N:1 entre Mensagem e Cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
