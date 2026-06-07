package com.taskflow.api.service;

import com.taskflow.api.dto.AtualizarCertificacoesDTO;
import com.taskflow.api.dto.CertificacaoDTO;
import com.taskflow.api.entity.Certificacao;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.repository.CertificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.taskflow.api.repository.ProjectManagerRepository;
import com.taskflow.api.repository.CertificacaoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificacaoService {
    private final ProjectManagerRepository projectManagerRepository;
    private final CertificacaoRepository certificacaoRepository;

    // adicionar lista de certificações (cadastro)
    @Transactional
    public void adicionar (UUID idManager, List<CertificacaoDTO> dtos){
        ProjectManager pm = projectManagerRepository.findById(idManager).orElseThrow(() -> new RuntimeException("PM não encontrado"));

        for (CertificacaoDTO dto : dtos) {
            Certificacao cert = new Certificacao();
            cert.setCertificacao(dto.certificacao());
            cert.setInstituicao(dto.instituicao());
            cert.setCodigoCertificacao(dto.codCertificacao());
            cert.setUrlComprovante(dto.urlComprovante());
            cert.setProjectManager(pm);

            certificacaoRepository.save(cert);
            pm.getCertificacoes().add(cert);
        }
        projectManagerRepository.save(pm);
    }

    public void atualizar(UUID id, AtualizarCertificacoesDTO dto){
        Certificacao certificacao = certificacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Certificação não encontrada"));

        if(dto.getNome() != null){
            certificacao.setCertificacao(dto.getNome());
        }
        if(dto.getInstituicao() != null){
            certificacao.setInstituicao(dto.getInstituicao());
        }
        if(dto.getCodigoCertificacao() != null){
            certificacao.setCodigoCertificacao(dto.getCodigoCertificacao());
        }
        if(dto.getUrlComprovante() != null){
            certificacao.setUrlComprovante(dto.getUrlComprovante());
        }
        certificacaoRepository.save(certificacao);
    }

    public void remover(UUID idCertificacao){
        Certificacao certificacao = certificacaoRepository.findById(idCertificacao).orElseThrow(() -> new RuntimeException("Certificação não encontrada"));
        certificacaoRepository.delete(certificacao);
    }

    // adicionar somente uma certificacao na tela de perfil
    public void adicionarCertificacao(UUID idManager, CertificacaoDTO dto){
        ProjectManager pm = projectManagerRepository.findById(idManager).orElseThrow(() -> new RuntimeException("PM não encontrado"));

        Certificacao certificacao = new Certificacao();
        certificacao.setCertificacao(dto.certificacao());
        certificacao.setInstituicao(dto.instituicao());
        certificacao.setCodigoCertificacao(dto.codCertificacao());
        certificacao.setUrlComprovante(dto.urlComprovante());
        certificacao.setProjectManager(pm);

        certificacaoRepository.save(certificacao);
        pm.getCertificacoes().add(certificacao);
        projectManagerRepository.save(pm);
    }
}
