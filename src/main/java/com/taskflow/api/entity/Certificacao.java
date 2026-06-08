package com.taskflow.api.entity;

import jakarta.persistence.*;
import java.util.UUID;

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

    public Certificacao() {
    }

    public UUID getIdCertificacao() {
        return idCertificacao;
    }

    public void setIdCertificacao(UUID idCertificacao) {
        this.idCertificacao = idCertificacao;
    }

    public String getCertificacao() {
        return certificacao;
    }

    public void setCertificacao(String certificacao) {
        this.certificacao = certificacao;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getCodigoCertificacao() {
        return codigoCertificacao;
    }

    public void setCodigoCertificacao(String codigoCertificacao) {
        this.codigoCertificacao = codigoCertificacao;
    }

    public String getUrlComprovante() {
        return urlComprovante;
    }

    public void setUrlComprovante(String urlComprovante) {
        this.urlComprovante = urlComprovante;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    // relação N:1 entre Certificacao e ProjectManager
    @ManyToOne
    @JoinColumn(name = "id_manager", nullable = false)
    private ProjectManager projectManager;
}
