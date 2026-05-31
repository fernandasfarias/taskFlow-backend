package com.taskflow.api.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "certificacao", schema = "projetos")
public class Certificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_certificacao")
    private UUID idCertificacao;

    @Column(nullable = false)
    private String certificacao;

    @Column(nullable = false)
    private String instituicao;

    // opcional pois nem todos os certificados possuem código
    @Column(name ="codigo_certificacao", nullable = true)
    private String codigoCertificacao;

    @Column(name = "url_comprovante", nullable = false)
    private String urlComprovante;

    // relação N:1 entre Certificacao e ProjectManager
    @ManyToOne
    @JoinColumn(name = "id_manager", nullable = false)
    private ProjectManager projectManager;
}
